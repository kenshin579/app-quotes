#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import argparse
import enum
import json
import os
import pprint
import re
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
API_QUOTE_URL = "http://localhost:8080/api/quotes/folders"
API_LOGIN_URL = "http://localhost:8080/api/auth/login"
DEFAULT_LANG = "kr"
TMP_DIR = '/tmp'
USERNAME = 'kenshin579'
PASSWORD = '123456'
REQUEST_FORM_DATA_BOUNDARY = "REQUEST_FORM_DATA_BOUNDARY"
FORM_DATA_STARTING_PAYLOAD = '--{0}\r\nContent-Disposition: form-data; name=\\"'.format(REQUEST_FORM_DATA_BOUNDARY)
FORM_DATA_MIDDLE_PAYLOAD = '\"\r\n\r\n'
FORM_DATA_ENDING_PAYLOAD = '--{0}--'.format(REQUEST_FORM_DATA_BOUNDARY)
REQUEST_CUSTOM_HEADER = {
    'content-type': "multipart/form-data; boundary={}".format(REQUEST_FORM_DATA_BOUNDARY),
    'Content-Type': "",
    'cache-control': "no-cache"
}


class PARSE_MODE(enum.Enum):
    INIT = 0
    START = 1
    END = 2


################################################################################################
# Functions
#
################################################################################################
def parse_quote_file(filename, lang=DEFAULT_LANG):
    parse_mode = PARSE_MODE.END
    print('filename -> ', filename)
    result = []
    line_count = 1
    with open(filename) as f:
        tags = []
        for line in f:
            line_count += 1
            if len(line.strip()) != 0:
                print('each', line_count, ':', parse_mode, 'line', line.strip())
                if parse_mode == PARSE_MODE.END and re.search('^#\s*tags\s*:', line) != None:
                    print('====> TAGS', line.strip())
                    parse_mode = PARSE_MODE.START
                    tags_line = line.strip().split(':')
                    for each_tag in tags_line[1].split(','):
                        tags.append(each_tag.strip())
                    print('tags', tags)
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

def generate_form_data_payload(kwargs):
    payload = ''
    for key, value in kwargs.items():
        payload += '{0}{1}{2}{3}\r\n'.format(FORM_DATA_STARTING_PAYLOAD, key, FORM_DATA_MIDDLE_PAYLOAD, value)
    payload += FORM_DATA_ENDING_PAYLOAD
    return payload


def send_quotes(url, folder_id, quote_list):
    pprint.pprint(quote_list)
    token = get_token(API_LOGIN_URL)
    headers = {
        'Authorization': token['token_TYPE'] + ' ' + token['accessToken']
    }
    for quote in quote_list:
        print('quote', quote)
        response = requests.post(url + '/' + folder_id, data=quote, headers=headers)
        print("response", response.json())

def get_token(url):
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    login_json = {
        "username": USERNAME,
        "password": PASSWORD
    }
    response = requests.post(url, data=json.dumps(login_json), headers=headers)
    print('response', response.json())
    return response.json()


def send_bulk_quotes(url, quote_list):
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    print('quote_list', quote_list)
    # r = requests.post(url, data=json.dumps(quote_list), headers=headers)
    # print("r", r)

################################################################################################
# Main function
#
################################################################################################

def main():
    parser = argparse.ArgumentParser(description="Uploading quotes to API server: " + API_QUOTE_URL)

    subparsers = parser.add_subparsers(help='commands', dest='subcommand')

    # epub subcommand
    epub_parser = subparsers.add_parser('epub', help='epub subcommand')
    # parser.add_argument("-e", "--epub", action="store", help="insert quote from a epub")

    # txt subcommand
    txt_parser = subparsers.add_parser('text', help='text subcommand')
    txt_parser.add_argument("-f", "--file", action="store", help="insert quote from a file")

    required_group = txt_parser.add_argument_group('required option')
    required_group.add_argument("--folderid", action='store', help="set folderid", required=True)

    # cafe subcommand
    # parser.add_argument("-t", "--tags", nargs='+', help="sending quotes to " + API_QUOTE_URL, required=True)

    args = parser.parse_args()
    print('args', args)

    if args.subcommand:
        if args.subcommand == 'text':
            if args.file:
                if os.path.exists(args.file):
                    result = parse_quote_file(args.file)
                    print('result', result)
                    send_quotes(API_QUOTE_URL, args.folderid, parse_quote_file(args.file))
                else:
                    print("filename not found: " + args.file)
        elif args.subcommand == 'epub':
            if args.epub:
                if os.path.exists(args.epub):
                    parse_quote_epub(args.epub)
                else:
                    print("filename not found: " + args.epub)

if __name__ == "__main__":
    sys.exit(main())
