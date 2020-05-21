#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import argparse
import enum
import json
import os
import pprint
import re
import sys

import requests

################################################################################################
# TODO:
# - DB에 이미 존재하는지 확인이 필요함
################################################################################################


################################################################################################
# Constants
#
################################################################################################
DATA_DIR = 'data'
API_QUOTE_URL = "http://localhost:8080/api/quotes"
API_LOGIN_URL = "http://localhost:8080/api/auth/login"
DEFAULT_LANG = "kr"
TMP_DIR='/tmp'

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
    parse_mode = PARSE_MODE.INIT
    print('filename -> ', filename)
    result = []
    line_count = 1
    with open(filename) as f:
        tags = []
        for line in f:
            print('each', line_count, ':', parse_mode, 'line', line.strip())
            line_count += 1
            if parse_mode == PARSE_MODE.START:
                if len(line.strip()) == 0 or re.search('^#\s*', line) != None:
                    parse_mode = PARSE_MODE.END
                    print('========================> END', line.strip())
                    tags = []
            if len(line.strip()) != 0:
                if parse_mode != PARSE_MODE.START and re.search('^#\s*tags\s*:', line) != None:
                    print('========================> START', line.strip())
                    parse_mode = PARSE_MODE.START
                    tags_line = line.strip().split(':')
                    for each_tag in tags_line[1].split(','):
                        tags.append(each_tag.strip())
                        # tags = tags + each_tag.strip() + ','
                    print('tags', tags)
                elif parse_mode == PARSE_MODE.START and re.search('^#\s*', line) == None:
                    print('parsing quote', line)
                    parse_line = line.strip().split('-')
                    print('parse_line', parse_line)
                    l = ['Jim', 'Dave', 'James', 'Laura', 'Kasra']
                    print('test', ','.join(l[:-1]) + f' & {l[-1]}')
                    result.append({
                        'quoteText': parse_line[0].strip(),
                        'authorName': parse_line[1].strip(),
                        'tags': ','.join(tags[:-1]) + f'{tags[-1]}',
                        'useYn': 'Y'
                    })

            for each_quote in result:
                print('result', each_quote)

    return result

# def unzip(filename):
    # cmd='unzip -d ' + TMP_DIR +

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


def send_one_quote(url, quote_list):
    pprint.pprint(quote_list)
    headers = {'Content-Type': 'multipart/form-data; charset=utf-8'}
    token = get_token(API_LOGIN_URL)
    # REQUEST_CUSTOM_HEADER['Authorization'] = token['token_TYPE'] + ' ' + token['accessToken']
    headers['Authorization'] = token['token_TYPE'] + ' ' + token['accessToken']
    print('headers', headers)
    for each_quote in quote_list:
        print('each_quote', each_quote)
        # request_data = generate_form_data_payload(each_quote)
        # response = requests.post(url, data=request_data, headers=REQUEST_CUSTOM_HEADER)
        # response = requests.post(url, data=request_data.encode('utf-8'), headers=REQUEST_CUSTOM_HEADER)
        session = requests.Session()
        response = session.post(url, json=each_quote, headers=headers)
        print("response", response.json())

def get_token(url):
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    login_json = {
        "username": "kenshin579",
        "password": "123456"
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

    # parser.add_argument("-t", "--tags", nargs='+', help="sending quotes to " + API_QUOTE_URL, required=True)
    parser.add_argument("-f", "--file", action="store", help="insert quote from a file")
    parser.add_argument("-e", "--epub", action="store", help="insert quote from a epub")

    args = parser.parse_args()
    print('args', args)

    if args.file:
        if os.path.exists(args.file):
            result = parse_quote_file(args.file)
            print('result', result)
            send_one_quote(API_QUOTE_URL, parse_quote_file(args.file))
        else:
            print("filename not found: " + args.file)
    elif args.epub:
        if os.path.exists(args.epub):
            parse_quote_epub(args.epub)
        else:
            print("filename not found: " + args.epub)


if __name__ == "__main__":
    sys.exit(main())
