from unittest import TestCase

import quotes


class QuotesTest(TestCase):
    def test_postprocess(self):
        print('test')
        result = quotes.postprocess('test')
        print('result', result)
        # self.fail()
