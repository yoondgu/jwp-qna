package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionFixture.Q1;
import static qna.domain.QuestionFixture.Q2;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@TestConstructor(autowireMode = AutowireMode.ALL)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class QuestionRepositoryTest {

    private final QuestionRepository questionRepository;

    public QuestionRepositoryTest(final QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @DisplayName("질문을 저장한다.")
    @Test
    void save() {
        // given
        // when
        final Question saved = questionRepository.save(Q1);

        // then
        assertThat(questionRepository.findById(saved.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @DisplayName("질문을 식별자 아이디로 조회한다.")
    @Test
    void findById() {
        // given
        final Question saved = questionRepository.save(Q1);

        // when
        final Question actual = questionRepository.findById(saved.getId()).get();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @DisplayName("삭제되지 않은 질문을 식별자 아이디로 조회한다.")
    @Test
    void findByIdAndDeletedFalse() {
        // given
        final Question saved = questionRepository.save(Q1);

        // when
        final Question actual = questionRepository.findByIdAndDeletedFalse(saved.getId()).get();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @DisplayName("삭제되지 않은 질문을 모두 조회한다.")
    @Test
    void findByDeletedFalse() {
        // given
        final Question saved = questionRepository.save(Q1);
        final Question saved2 = questionRepository.save(Q2);

        // when
        final List<Question> deletedFalse = questionRepository.findByDeletedFalse();

        // then
        assertThat(deletedFalse)
                .containsOnly(saved, saved2);
    }

    @DisplayName("질문의 삭제 여부 변경을 감지해 갱신한다.")
    @Test
    void updateDeleted() {
        // given
        final Question saved = questionRepository.save(Q1);

        // when
        saved.setDeleted(true);

        // then
        final Question updated = questionRepository.findById(saved.getId()).get();
        assertThat(updated.isDeleted())
                .isTrue();
    }

    @DisplayName("식별자로 조회한 질문은 서로 동일하다.")
    @Test
    void identity() {
        // given
        final Question saved = questionRepository.save(Q1);
        final Question actual = questionRepository.findById(saved.getId()).get();
        final Question actual2 = questionRepository.findByIdAndDeletedFalse(saved.getId()).get();

        // when
        // then
        assertThat(saved == actual).isTrue();
        assertThat(actual == actual2).isTrue();
    }
}
