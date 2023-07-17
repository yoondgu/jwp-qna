package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserFixture.JAVAJIGI;
import static qna.domain.UserFixture.SANJIGI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswerTest {

    @DisplayName("답변을 삭제 상태로 변경할 수 있다.")
    @Test
    void deleteSuccess() throws CannotDeleteException {
        // given
        Question question = new Question("title", "question contents").writeBy(JAVAJIGI);
        Answer answer = new Answer(JAVAJIGI, question, "answer contents");

        // when
        DeleteHistory deleteHistory = answer.deleteBy(JAVAJIGI);

        // then
        assertThat(deleteHistory).isEqualTo(
                new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter())
        );
    }

    @DisplayName("다른 사용자의 답변을 삭제할 수 없다.")
    @Test
    void deleteFailByOtherUser() {
        // given
        Question question = new Question("title", "question contents").writeBy(JAVAJIGI);
        Answer answer = new Answer(JAVAJIGI, question, "answer contents");

        // when
        // then
        assertThatThrownBy(() -> answer.deleteBy(SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
