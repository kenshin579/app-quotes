from unittest import TestCase

import quotes


class QuotesTest(TestCase):
    def test_postprocess_munganbot(self):
        result = quotes.postprocess('봇의특성상트윗이중복될수있습니다')
        self.assertEqual(result, '')

    def test_postprocess_munganbot2(self):
        result = quotes.postprocess('* text.')
        self.assertEqual(result, 'text')

        result = quotes.postprocess('* 충고는 눈과 같아 조용히 내리면 내릴수록 마음에 오래 남고 가슴에 더욱 깊이 새겨진다.')
        self.assertEqual(result, '충고는 눈과 같아 조용히 내리면 내릴수록 마음에 오래 남고 가슴에 더욱 깊이 새겨진다')
