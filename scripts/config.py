import os

credentials = dict(
    consumer_key=os.environ.get('CONSUMER_KEY'),
    consumer_secret=os.environ.get('CONSUMER_SECRET'),
    token_secret=os.environ.get('TOKEN_SECRET'),
    access_token=os.environ.get('ACCESS_TOKEN'),
    quote_username=os.environ.get('QUOTE_USERNAME'),
    quote_password=os.environ.get('QUOTE_PASSWORD')
)

print('credentials', credentials)