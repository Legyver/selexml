package com.legyver.selexml.query.where.comparator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LikeComparatorTest {

    @Test
    public void simpleLike() {
        {
            LikeComparator likeComparator = new LikeComparator("K%", false);
            assertThat(likeComparator.matches("Kaplan")).isEqualTo(true);
            assertThat(likeComparator.matches("AK47")).isEqualTo(false);
        }
        {
            LikeComparator likeComparator = new LikeComparator("k%", false);
            assertThat(likeComparator.matches("Kaplan")).isEqualTo(false);
            assertThat(likeComparator.matches("AK47")).isEqualTo(false);
        }
        {
            LikeComparator likeComparator = new LikeComparator("_K%", false);
            assertThat(likeComparator.matches("Kaplan")).isEqualTo(false);
            assertThat(likeComparator.matches("AK47")).isEqualTo(true);
        }
        {
            LikeComparator likeComparator = new LikeComparator("%apl%", false);
            assertThat(likeComparator.matches("Kaplan")).isEqualTo(true);
            assertThat(likeComparator.matches("AK47")).isEqualTo(false);
        }
        {
            LikeComparator likeComparator = new LikeComparator("_K__", false);
            assertThat(likeComparator.matches("Kaplan")).isEqualTo(false);
            assertThat(likeComparator.matches("AK47")).isEqualTo(true);
        }
    }

    @Test
    public void caseInsensitiveLike() {
        {
            LikeComparator likeComparator = new LikeComparator("k%", true);
            assertThat(likeComparator.matches("Kaplan")).isEqualTo(true);
            assertThat(likeComparator.matches("AK47")).isEqualTo(false);
        }
        {
            LikeComparator likeComparator = new LikeComparator("_k%", true);
            assertThat(likeComparator.matches("Kaplan")).isEqualTo(false);
            assertThat(likeComparator.matches("AK47")).isEqualTo(true);
        }
        {
            LikeComparator likeComparator = new LikeComparator("%APl%", true);
            assertThat(likeComparator.matches("Kaplan")).isEqualTo(true);
            assertThat(likeComparator.matches("AK47")).isEqualTo(false);
        }
        {
            LikeComparator likeComparator = new LikeComparator("_k__", true);
            assertThat(likeComparator.matches("Kaplan")).isEqualTo(false);
            assertThat(likeComparator.matches("AK47")).isEqualTo(true);
        }
    }

}
