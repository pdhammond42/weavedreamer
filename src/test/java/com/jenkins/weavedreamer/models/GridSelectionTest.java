package com.jenkins.weavedreamer.models;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by pete on 22/10/18.
 */
public class GridSelectionTest {
    @Test
    public void selectionIsHalfClosedRange() {
        GridSelection selection = new GridSelection(1, 4, 3,7 );
        assertThat(selection.getColumns(), equalTo(3));
        assertThat(selection.getRows(), equalTo(2));
        assertThat(selection.contains(1, 3), is(false));
        assertThat(selection.contains(1, 4), is(true));
        assertThat(selection.contains(1, 6), is(true));
        assertThat(selection.contains(1, 7), is(false));

        assertThat(selection.contains(0, 4), is(false));
        assertThat(selection.contains(1, 4), is(true));
        assertThat(selection.contains(2, 4), is(true));
        assertThat(selection.contains(3, 4), is(false));
    }

    @Test
    public void selectionWorksLeftUp() {
        GridSelection selection = new GridSelection(2, 6, 0,3 );
        assertThat(selection.getColumns(), equalTo(3));
        assertThat(selection.getRows(), equalTo(2));
        assertThat(selection.contains(1, 3), is(false));
        assertThat(selection.contains(1, 4), is(true));
        assertThat(selection.contains(1, 6), is(true));
        assertThat(selection.contains(1, 7), is(false));

        assertThat(selection.contains(0, 4), is(false));
        assertThat(selection.contains(1, 4), is(true));
        assertThat(selection.contains(2, 4), is(true));
        assertThat(selection.contains(3, 4), is(false));
    }
}
