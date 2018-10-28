#!/usr/bin/env python3
# -*- coding: utf-8 -*-


################################################################################################
# quotes를 API 서버에 upload하는 script
# TODO:
################################################################################################


################################################################################################
# Constants
#
################################################################################################
import sys

URL_QUOTE_SERVER = "http://localhost:8080/ap/v1/new"


################################################################################################
# Main function
#
################################################################################################
import argparse


def main():
    parser = argparse.ArgumentParser(description="Uploading quotes to API")

    parser.add_argument("-u", "--upload", action='store_true',
                        help="sending quotes to " + URL_QUOTE_SERVER)

    print(parser.parse_args())

    args = parser.parse_args()

    if args.upload:
        print("uploading...")


if __name__ == "__main__":
    sys.exit(main())
