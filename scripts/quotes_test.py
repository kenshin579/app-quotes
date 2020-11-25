from unittest import TestCase

import quotes


class QuotesTest(TestCase):
    def test_postprocess_munganbot(self):
        result = quotes.postprocess('봇의특성상트윗이중복될수있습니다')
        self.assertEqual('', result)

    def test_postprocess_munganbot2(self):
        result = quotes.postprocess('* text.')
        self.assertEqual('text', result)

        result = quotes.postprocess('* 충고는 눈과 같아 조용히 내리면 내릴수록 마음에 오래 남고 가슴에 더욱 깊이 새겨진다.')
        self.assertEqual('충고는 눈과 같아 조용히 내리면 내릴수록 마음에 오래 남고 가슴에 더욱 깊이 새겨진다', result)

    def test_postprocess_munganbot3(self):
        result = quotes.postprocess('text. \n\n\n')
        self.assertEqual('text', result)

    def test_postprocess_munganbot4(self):
        result = quotes.postprocess('text.   ')
        self.assertEqual('text', result)

    def test_postprocess_munganbot5(self):
        result = quotes.postprocess('text   ')
        self.assertEqual('text', result)

    def test_postprocess_munganbot6(self):
        result = quotes.postprocess('this is a test.\n(wow) this is a test \n\n\n ')
        self.assertEqual('this is a test.\n(wow) this is a test', result)

    def test_postprocess_munganbot7(self):
        result = quotes.postprocess('사랑은 있거나, 없다.\n(둘 중 하나다.) 가벼운 사랑은 아예 사랑이 아니다 \n\n\n ')
        self.assertEqual('사랑은 있거나, 없다.\n(둘 중 하나다.) 가벼운 사랑은 아예 사랑이 아니다', result)

    def test_parse_quote(self):
        result = quotes.parse_quote('사랑은 있거나, 없다.\n(둘 중 하나다.) 가벼운 사랑은 아예 사랑이 아니다 \n\n\n -토니 모리슨\n')
        self.assertEqual('사랑은 있거나, 없다.\n(둘 중 하나다.) 가벼운 사랑은 아예 사랑이 아니다', result['quote'])
        self.assertEqual('토니 모리슨', result['author'])

    def test_parse_quote2(self):
        result = quotes.parse_quote('quote text\n\n\n - Frank')
        self.assertEqual('quote text', result['quote'])
        self.assertEqual('Frank', result['author'])

    def test_parse_quote3(self):
        result = quotes.parse_quote('사랑은 무엇보다도 \n 자신을 위한 선물이다.\n\n\n-장 아누이')
        self.assertEqual('사랑은 무엇보다도 \n 자신을 위한 선물이다', result['quote'])
        self.assertEqual('장 아누이', result['author'])

    def test_parse_quote_author_two_dashes(self):
        result = quotes.parse_quote('사랑은 무엇보다도 \n 자신을 위한 선물이다.\n\n\n- 장 아누이 -')
        self.assertEqual('사랑은 무엇보다도 \n 자신을 위한 선물이다', result['quote'])
        self.assertEqual('장 아누이', result['author'])

    def test_parse_quote_author_quote_with_question_mark(self):
        result = quotes.parse_quote('성공한 사람이 될 수 있는데 왜 평범한 이에 머무르려 하는가?\n\n-베르톨트 브레히트')
        self.assertEqual('성공한 사람이 될 수 있는데 왜 평범한 이에 머무르려 하는가?', result['quote'])
        self.assertEqual('베르톨트 브레히트', result['author'])

    def test_parse_quote_no_author(self):
        result = quotes.parse_quote('quote text\n\n\n')
        self.assertEqual('quote text', result['quote'])

    def test_str_contains_in_the_lists(self):
        TAGS_CHRISTIAN = ['하느님', '하나님', '주님']
        res = [ele for ele in TAGS_CHRISTIAN if (ele in '은 하느님')]
        self.assertTrue(str(bool(res)))
