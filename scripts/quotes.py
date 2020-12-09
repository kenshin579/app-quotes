#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import argparse
import enum
import getpass
import glob
import json
import logging
import os
import pprint
import random
import re
import shutil
import sys
import time

import confighelper
import requests
import config

################################################################################################
# TODO:
# - DB에 이미 존재하는지 확인이 필요함
################################################################################################


################################################################################################
# Constants
#
################################################################################################
import tweepy

DATA_DIR = 'data'
HOSTNAME_LOCAL = 'http://localhost:8080'
HOSTNAME_REAL = 'https://quote.advenoh.pe.kr'

API_QUOTE_URL = '/api/quotes/folders'
API_LOGIN_URL = '/api/auth/login'
API_RANDOM_URL = '/api/quotes/random'
API_QUOTE_EXISTS_URL = '/api/quotes/checkQuoteExists'
DEFAULT_LANG = "kr"
TMP_DIR = '/tmp'
TWITTER_QUOTE_ACCOUNTS = ["munganbot", "Famoussay_bot", "nsw1223", "majuwang"]
TAGS_CHRISTIAN = ['하느님', '하나님', '주님', '주께서']
MAX_SLEEP_TIME = 10

class PARSE_MODE(enum.Enum):
    START = 1
    END = 2


class PasswordPromptAction(argparse.Action):
    def __init__(self,
                 option_strings,
                 dest=None,
                 nargs=0,
                 default=None,
                 required=False,
                 type=None,
                 metavar=None,
                 help=None):
        super(PasswordPromptAction, self).__init__(
            option_strings=option_strings,
            dest=dest,
            nargs=nargs,
            default=default,
            required=required,
            metavar=metavar,
            type=type,
            help=help)

    def __call__(self, parser, args, values, option_string=None):
        password = getpass.getpass()
        setattr(args, self.dest, password)


################################################################################################
# Functions
#
################################################################################################
def parse_quote_file(filename):
    parse_mode = PARSE_MODE.END
    logging.debug('filename -> %s', filename)
    result = []
    line_count = 1
    with open(filename) as f:
        tags = []
        for line in f:
            line_count += 1
            if len(line.strip()) != 0:
                # print('each', line_count, ':', parse_mode, 'line', line.strip())
                if parse_mode == PARSE_MODE.END and re.search('^#\s*tags\s*:', line) != None:
                    logging.debug('====> TAGS: %s', line.strip())
                    parse_mode = PARSE_MODE.START
                    tags_line = line.strip().split(':')
                    for each_tag in tags_line[1].split(','):
                        tags.append(each_tag.strip())
                    # print('tags', tags)
                elif parse_mode == PARSE_MODE.START and re.search('^#\s*', line) == None:
                    logging.debug('====> QUOTE: %s', line)
                    parse_mode = PARSE_MODE.END
                    parse_line = line.strip().split('-')
                    result.append({
                        'quoteText': parse_line[0].strip(),
                        'authorName': parse_line[1].strip(),
                        'tags': ','.join(tags),
                        'useYn': 'Y'
                    })
                    tags = []
    return result


def parse_quote_epub(filename):
    print('filename -> ', filename)
    result = []
    # with open(filename) as f:
    #     for line in f:
    #         print('line', line)
    return result


def get_baseurl(phase):
    if phase == 'local':
        return HOSTNAME_LOCAL
    elif phase == 'real':
        return HOSTNAME_REAL
    else:
        logging.error('no server : %s', phase)


def get_token(url, username, password):
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    login_json = {
        "username": username,
        "password": password
    }
    # print('login_json', login_json)
    # print('url', url)
    response = requests.post(url, data=json.dumps(login_json), headers=headers)
    return response.json()


def post_quotes(hostname_url, username, password, folder_id, quote_list):
    pprint.pprint(quote_list)
    token = get_token(hostname_url + API_LOGIN_URL, username, password)
    headers = {
        'Authorization': token['token_TYPE'] + ' ' + token['accessToken']
    }
    for quote in quote_list:
        logging.debug('quote : %s', quote)
        response = requests.post(hostname_url + API_QUOTE_URL + '/' + folder_id, data=quote, headers=headers)
        logging.debug("response", response.json())


def send_bulk_quotes(url, quote_list):
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    logging.debug('quote_list : %s', quote_list)
    # r = requests.post(url, data=json.dumps(quote_list), headers=headers)
    # print("r", r)


def get_random_quote(url):
    logging.debug('url : %s', url)
    res = requests.get(url)
    return res.json()


def move_file(src, dst):
    if os.path.isfile(src):
        if os.path.isdir(dst):
            print(src, '->', dst)
            shutil.move(src, dst)
        else:
            logging.info('not found :: dst : %s', dst)
    else:
        logging.info('not found :: source file : %s', src)


def send_quote_twitter(twitter_config, url_random_quote):
    logging.debug('twitter_config : %s', twitter_config)
    twitter_api = create_tweepy_api(twitter_config)

    # get random quote
    quote = get_random_quote(url_random_quote)
    quote_text = quote['quoteText']

    if quote['authorName']:
        quote_text += "\n\n- " + quote['authorName']

    logging.debug('quote_text : %s', quote_text)
    # Create API object

    twitter_api.update_status(quote_text)


def create_tweepy_api(twitter_config):
    auth = tweepy.OAuthHandler(twitter_config["consumer_key"], twitter_config["consumer_secret"])
    auth.set_access_token(twitter_config["access_token"], twitter_config["token_secret"])
    twitter_api = tweepy.API(auth)
    return twitter_api


def postprocess(text):
    '''
    - munganbot에서 제외 목록
    1.봇의특성상트윗이중복될수있습니다
    2.* 충고는 눈과 같아 조용히 내리면 내릴수록 마음에 오래 남고 가슴에 더욱 깊이 새겨진다.

    참고
    - https://regex101.com/r/Nagr3R/1/
    - https://stackoverflow.com/questions/33889952/python-re-sub-multiline-on-string

    :param text:
    :return:
    '''
    if text in ['봇의특성상트윗이중복될수있습니다']:
        return ''
    result = re.sub('^[*\s]*(.*?)[.\s]*$', '\\1', text, flags=re.S)
    # logging.debug('result: %s', result)

    return result


def random_sleep(max_sleep_time_in_secs):
    sleep_time = random.randrange(1, max_sleep_time_in_secs, 1)
    logging.debug('sleep... %s', sleep_time)
    time.sleep(sleep_time)


def parse_quote(text):
    '''
    text를 명언과 저자로 parse해준다
    :param text:
    :return:
    '''

    pattern_list = [
        '^[\s▶]*([\w()!\"%,*.?\n ]+)[-\s]*([\w ]*)[-\s#\w]*$'
    ]

    result = {}
    for pattern in pattern_list:
        matched_quote_author = re.search(pattern, text)
        if matched_quote_author is not None:
            result['quote'] = postprocess(matched_quote_author.group(1))
            if matched_quote_author.group(2) is not None:
                result['author'] = postprocess(matched_quote_author.group(2))

        logging.debug('result: %s', result)
    return result


def check_quote_exists(base_url, quote):
    params = {
        'quoteText': quote
    }
    res = requests.get(base_url + API_QUOTE_EXISTS_URL, params=params)
    # logging.debug('res : %s', res.json())
    return res.json()


def save_quote_from_twitter(env_config, base_url, folder_id, username=None, password=None):
    quote_list = []
    twitter_api = create_tweepy_api(env_config)
    username = username or env_config['quote_username']
    password = password or env_config['quote_password']

    logging.info('username : %s', username)

    # twitter에서 명언 가져오기
    for twitter_id in TWITTER_QUOTE_ACCOUNTS:
        logging.debug('twitter_id: %s', twitter_id)
        timeline = twitter_api.user_timeline(twitter_api.user_timeline, screen_name='@' + twitter_id)
        for status in timeline:
            parsed_str = parse_quote(status.text)

            if 'quote' in parsed_str:
                logging.debug('quote : %s', parsed_str['quote'])
                logging.debug('author : %s', parsed_str['author'])
                quote = {
                    'quoteText': parsed_str['quote'],
                    'authorName': parsed_str['author'],
                    'useYn': 'Y'
                }

                is_christian_tags_exists = bool([ele for ele in TAGS_CHRISTIAN if (ele in parsed_str['quote'])])
                if is_christian_tags_exists:
                    quote['tags'] = '성경'
                quote_list.append(quote)
            else:
                logging.warning('parse error : %s', status.text)
        random_sleep(MAX_SLEEP_TIME)
        print('')

    logging.info('quote_list.size : %s', len(quote_list))
    count = 0
    for each_quote in quote_list:
        count += 1
        logging.info('count : %s', count)
        if not check_quote_exists(base_url, each_quote['quoteText']):
            post_quotes(base_url, username, password, folder_id, quote_list)


################################################################################################
# Main function
#
################################################################################################


def main():
    parser = argparse.ArgumentParser(description="Uploading quotes to API server")

    subparsers = parser.add_subparsers(help='commands', dest='subcommand')

    # epub subcommand
    epub_parser = subparsers.add_parser('epub', help='epub subcommand')
    # parser.add_argument("-e", "--epub", action="store", help="insert quote from a epub")

    # txt subcommand
    txt_parser = subparsers.add_parser('text', help='text subcommand')
    required_group = txt_parser.add_argument_group('required option')
    required_group.add_argument("--folderid", action='store', help="set folderid", required=True)
    required_group.add_argument("-s", "--src", action="store", help="source file or directory name", required=True)
    required_group.add_argument("-d", "--dst", action="store", help="destination directory (after upload)",
                                required=True)
    required_group.add_argument("--server", action='store', choices=["local", "real"],
                                help="set server information for the action to carry on",
                                required=True)
    required_group.add_argument('-u', dest='username', type=str, required=True)
    required_group.add_argument('-p', dest='password', action=PasswordPromptAction, type=str, required=True)

    # twitter subcommand
    twitter_parser = subparsers.add_parser('twitter', help='twitter subcommand')
    required_twitter_group = twitter_parser.add_argument_group('required options')
    required_twitter_group.add_argument('-c', '--config', dest='config_file', metavar='PATH', default=None,
                                        type=str, help='config file (yaml)')

    required_twitter_group.add_argument("--folderid", action='store', help="set folderid", required=True)
    required_twitter_group.add_argument("--server", action='store', choices=["local", "real"],
                                        help="set server information for the action to carry on",
                                        required=True)

    twitter_parser.add_argument('-u', dest='username', type=str)
    twitter_parser.add_argument('-p', dest='password', action=PasswordPromptAction, type=str)

    twitter_parser.add_argument("--upload", action="store_true", help="send quote to twitter")
    twitter_parser.add_argument("--save", action="store_true", help="save quote from twitter")

    args = parser.parse_args()
    logging.info('args: %s', args)

    if args.subcommand:
        if args.subcommand == 'text':
            if args.src:
                if os.path.exists(args.src):
                    if os.path.isfile(args.src):
                        post_quotes(get_baseurl(args.server), args.username, args.password, args.folderid,
                                    parse_quote_file(args.src))
                        move_file(args.src, args.dst)
                    elif os.path.isdir(args.src):
                        for file in glob.glob(os.path.join(args.src, "*.txt")):
                            if os.path.isfile(file):
                                post_quotes(get_baseurl(args.server), args.username, args.password, args.folderid,
                                            parse_quote_file(file))
                                move_file(file, args.dst)
                else:
                    logging.info("filename not found: %s", args.file)
        elif args.subcommand == 'twitter':
            if args.upload:
                url_random_quote = get_baseurl(args.server) + API_RANDOM_URL
                if args.config_file:
                    local_file = confighelper.configure('quote', config_file=args.config_file)

                    send_quote_twitter(local_file, url_random_quote)
                else:
                    send_quote_twitter(config.credentials, url_random_quote)
            elif args.save:
                base_url = get_baseurl(args.server)
                if args.config_file:
                    local_file = confighelper.configure('quote', config_file=args.config_file)
                    save_quote_from_twitter(local_file, base_url, args.folderid, args.username, args.password)
                else:
                    save_quote_from_twitter(config.credentials, base_url, args.folderid)

        elif args.subcommand == 'epub':
            if args.epub:
                if os.path.exists(args.epub):
                    parse_quote_epub(args.epub)
                else:
                    logging.info("filename not found: %s", args.epub)


if __name__ == "__main__":
    format = '[%(asctime)s,%(msecs)d] [%(levelname)-4s] %(filename)s:%(funcName)s:%(lineno)d %(message)s'
    logging.basicConfig(format=format, level=logging.INFO,
                        datefmt='%Y-%m-%d:%H:%M:%S')
    sys.exit(main())
