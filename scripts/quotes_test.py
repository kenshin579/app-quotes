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

    def test_parse_quote(self):
        result = quotes.parse_quote('사랑은 있거나, 없다.\n(둘 중 하나다.) 가벼운 사랑은 아예 사랑이 아니다 \n\n\n -토니 모리슨\n')
        self.assertEqual(result[0], '사랑은 있거나, 없다.\n(둘 중 하나다.) 가벼운 사랑은 아예 사랑이 아니다 \n\n\n')
        self.assertEqual(result[1], '토니 모리슨')

    def test_parse_quote3(self):
        result = quotes.parse_quote('사랑은 무엇보다도 \n 자신을 위한 선물이다.\n\n\n-장 아누이')
        self.assertEqual(result[0], '사랑은 있거나, 없다.\n(둘 중 하나다.) 가벼운 사랑은 아예 사랑이 아니다 \n\n\n')
        self.assertEqual(result[1], '토니 모리슨')

    def test_parse_quote2(self):
        result = quotes.parse_quote('quote text\n\n\n - Frank')
        self.assertEqual(result[0], 'quote text')
        self.assertEqual(result[1], 'Frank')

