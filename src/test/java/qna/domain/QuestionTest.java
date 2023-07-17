package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserFixture.JAVAJIGI;
import static qna.domain.UserFixture.SANJIGI;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class QuestionTest {

    @DisplayName("질문을 삭제 상태로 변경할 수 있다.")
    @Test
    void deleteSuccess() throws CannotDeleteException {
        // given
        Question question = new Question("title", "question contents").writeBy(JAVAJIGI);
        question.addAnswer(new Answer(JAVAJIGI, question, "answer contents"));

        // when
        List<DeleteHistory> deleteHistory = question.deleteBy(JAVAJIGI);

        // then
        assertThat(deleteHistory).contains(
                new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter())
        );
    }

    @DisplayName("다른 사용자의 질문을 삭제할 수 없다.")
    @Test
    void deleteFailByOtherUser() {
        // given
        Question question = new Question("title", "question contents").writeBy(JAVAJIGI);
        question.addAnswer(new Answer(JAVAJIGI, question, "answer contents"));

        // when
        // then
        assertThatThrownBy(() -> question.deleteBy(SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("다른 사람이 쓴 답변이 있으면 삭제할 수 없다.")
    @Test
    void deleteFailByOtherUserAnswer() {
        // given
        Question question = new Question("title", "question contents").writeBy(JAVAJIGI);
        question.addAnswer(new Answer(SANJIGI, question, "answer contents"));

        // when
        // then
        assertThatThrownBy(() -> question.deleteBy(JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
