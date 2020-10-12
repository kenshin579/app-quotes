#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import argparse
import enum
import getpass
import glob
import json
import os
import pprint
import re
import shutil
import sys

################################################################################################
# TODO:
# - DB에 이미 존재하는지 확인이 필요함
################################################################################################


################################################################################################
# Constants
#
################################################################################################
import requests
from requests import Request, Session

DATA_DIR = 'data'
HOSTNAME_LOCAL = 'http://localhost:8080'
HOSTNAME_REAL = 'http://quote.advenoh.pe.kr'

API_QUOTE_URL = '/api/quotes/folders'
API_LOGIN_URL = '/api/auth/login'
API_RANDOM_URL = '/api/quote/random'
DEFAULT_LANG = "kr"
TMP_DIR = '/tmp'


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
    print('filename -> ', filename)
    result = []
    line_count = 1
    with open(filename) as f:
        tags = []
        for line in f:
            line_count += 1
            if len(line.strip()) != 0:
                # print('each', line_count, ':', parse_mode, 'line', line.strip())
                if parse_mode == PARSE_MODE.END and re.search('^#\s*tags\s*:', line) != None:
                    print('====> TAGS', line.strip())
                    parse_mode = PARSE_MODE.START
                    tags_line = line.strip().split(':')
                    for each_tag in tags_line[1].split(','):
                        tags.append(each_tag.strip())
                    # print('tags', tags)
                elif parse_mode == PARSE_MODE.START and re.search('^#\s*', line) == None:
                    print('====> QUOTE', line)
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
        print('no server', phase)


def get_token(url, username, password):
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    login_json = {
        "username": username,
        "password": password
    }
    print('login_json', login_json)
    print('url', url)
    response = requests.post(url, data=json.dumps(login_json), headers=headers)
    return response.json()


def post_quotes(hostname_url, username, password, folder_id, quote_list):
    pprint.pprint(quote_list)
    token = get_token(hostname_url + API_LOGIN_URL, username, password)
    headers = {
        'Authorization': token['token_TYPE'] + ' ' + token['accessToken']
    }
    for quote in quote_list:
        print('quote', quote)
        response = requests.post(hostname_url + API_QUOTE_URL + '/' + folder_id, data=quote, headers=headers)
        # print("response", response.json())


def send_bulk_quotes(url, quote_list):
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    print('quote_list', quote_list)
    # r = requests.post(url, data=json.dumps(quote_list), headers=headers)
    # print("r", r)


################################################################################################
# Main function
#
################################################################################################

def move_file(src, dst):
    if os.path.isfile(src):
        if os.path.isdir(dst):
            print(src, '->', dst)
            shutil.move(src, dst)
        else:
            print('not found :: dst', dst)
    else:
        print('not found :: source file', src)


def send_quite_twitter():
    print('sending twitter')
    pass


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

    # twitter upload
    twitter_parser = subparsers.add_parser('twitter', help='twitter subcommand')
    twitter_parser.add_argument("-u", "--upload", action="store_true", help="send quote to twiter")

    args = parser.parse_args()
    print('args', args)

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
                    print("filename not found: " + args.file)
        elif args.subcommand == 'twitter':
            send_quite_twitter()
        elif args.subcommand == 'epub':
            if args.epub:
                if os.path.exists(args.epub):
                    parse_quote_epub(args.epub)
                else:
                    print("filename not found: " + args.epub)


if __name__ == "__main__":
    sys.exit(main())
