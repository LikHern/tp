package foodwhere.logic.commands;

import static foodwhere.logic.commands.CommandTestUtil.assertCommandFailure;
import static foodwhere.logic.commands.CommandTestUtil.assertCommandSuccess;
import static foodwhere.logic.commands.REditCommand.MESSAGE_INVALID_INDEX_ERROR;
//import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import foodwhere.commons.core.index.Index;
import foodwhere.model.AddressBook;
import foodwhere.model.Model;
import foodwhere.model.ModelManager;
import foodwhere.model.UserPrefs;
import foodwhere.model.review.Review;
import foodwhere.model.review.ReviewBuilder;
import foodwhere.testutil.EditReviewDescriptorBuilder;
import foodwhere.testutil.TypicalIndexes;
import foodwhere.testutil.TypicalStalls;

/**
 * Contains integration tests (interaction with the Model) and unit tests for REditCommand.
 */
public class REditCommandTest {

    private Model model = new ModelManager(TypicalStalls.getTypicalAddressBook(), new UserPrefs());

    /*
    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Review editedReview = new ReviewBuilder().build();
        REditCommand.EditReviewDescriptor descriptor = new EditReviewDescriptorBuilder(editedReview).build();
        REditCommand rEditCommand = new REditCommand(TypicalIndexes.INDEX_FIRST_REVIEW, descriptor);

        String expectedMessage = String.format(REditCommand.MESSAGE_EDIT_REVIEW_SUCCESS, editedReview);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        expectedModel.setReview(model.getFilteredReviewList()
                .get(model.getFilteredReviewList().size() - 1), editedReview);

        assertCommandSuccess(rEditCommand, model, expectedMessage, expectedModel);
    }
   */

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastReview = Index.fromOneBased(model.getFilteredReviewList().size());
        Review lastReview = model.getFilteredReviewList().get(indexLastReview.getZeroBased());

        ReviewBuilder reviewInList = new ReviewBuilder(lastReview);
        Review editedReview =
                reviewInList.withContent(CommandTestUtil.VALID_CONTENT_BOB)
                        .withTags(CommandTestUtil.VALID_TAG_HUSBAND).build();

        REditCommand.EditReviewDescriptor descriptor =
                new EditReviewDescriptorBuilder().withContent(CommandTestUtil.VALID_CONTENT_BOB)
                        .withTags(CommandTestUtil.VALID_TAG_HUSBAND).build();
        REditCommand sEditCommand = new REditCommand(indexLastReview, descriptor);

        String expectedMessage = String.format(REditCommand.MESSAGE_EDIT_REVIEW_SUCCESS, editedReview);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setReview(lastReview, editedReview);

        assertCommandSuccess(sEditCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_noFieldSpecifiedUnfilteredList_failure() {
        REditCommand sEditCommand =
                new REditCommand(TypicalIndexes.INDEX_FIRST_REVIEW, new REditCommand.EditReviewDescriptor());

        assertCommandFailure(sEditCommand, model, REditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void execute_filteredList_success() {
        CommandTestUtil.showReviewAtIndex(model, TypicalIndexes.INDEX_FIRST_REVIEW);

        Review reviewInFilteredList =
                model.getFilteredReviewList().get(TypicalIndexes.INDEX_FIRST_REVIEW.getZeroBased());
        Review editedReview = new ReviewBuilder(reviewInFilteredList)
                .withContent(CommandTestUtil.VALID_CONTENT_BOB).build();
        REditCommand sEditCommand = new REditCommand(TypicalIndexes.INDEX_FIRST_REVIEW,
                new EditReviewDescriptorBuilder().withContent(CommandTestUtil.VALID_CONTENT_BOB).build());

        String expectedMessage = String.format(REditCommand.MESSAGE_EDIT_REVIEW_SUCCESS, editedReview);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setReview(model.getFilteredReviewList().get(0), editedReview);

        assertCommandSuccess(sEditCommand, model, expectedMessage, expectedModel);
    }

    /*
    @Test
    public void execute_duplicateReviewUnfilteredList_failure() {
        Review firstReview = model.getFilteredReviewList().get(TypicalIndexes.INDEX_FIRST_REVIEW.getZeroBased());
        REditCommand.EditReviewDescriptor descriptor = new EditReviewDescriptorBuilder(firstReview).build();
        REditCommand sEditCommand = new REditCommand(TypicalIndexes.INDEX_SECOND_REVIEW, descriptor);

        assertCommandFailure(sEditCommand, model, REditCommand.MESSAGE_DUPLICATE_REVIEW);
    }

     */

    /*
    @Test
    public void execute_duplicateReviewFilteredList_failure() {
        CommandTestUtil.showReviewAtIndex(model, TypicalIndexes.INDEX_FIRST_REVIEW);

        // edit review in filtered list into a duplicate in address book
        Review reviewInList =
                model.getAddressBook().getReviewList().get(TypicalIndexes.INDEX_SECOND_REVIEW.getZeroBased());
        REditCommand sEditCommand = new REditCommand(TypicalIndexes.INDEX_FIRST_REVIEW,
                new EditReviewDescriptorBuilder(reviewInList).build());

        assertCommandFailure(sEditCommand, model, REditCommand.MESSAGE_DUPLICATE_REVIEW);
    }

     */

    @Test
    public void execute_invalidReviewIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredReviewList().size() + 1);
        REditCommand.EditReviewDescriptor descriptor =
                new EditReviewDescriptorBuilder().withContent(CommandTestUtil.VALID_CONTENT_BOB).build();
        REditCommand sEditCommand = new REditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(sEditCommand, model, MESSAGE_INVALID_INDEX_ERROR);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidReviewIndexFilteredList_failure() {
        CommandTestUtil.showReviewAtIndex(model, TypicalIndexes.INDEX_FIRST_REVIEW);
        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_REVIEW;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getReviewList().size());

        REditCommand sEditCommand = new REditCommand(outOfBoundIndex,
                new EditReviewDescriptorBuilder().withContent(CommandTestUtil.VALID_CONTENT_BOB).build());

        assertCommandFailure(sEditCommand, model, MESSAGE_INVALID_INDEX_ERROR);
    }

    /*
    @Test
    public void equals() {
        final REditCommand standardCommand =
                new REditCommand(TypicalIndexes.INDEX_FIRST_REVIEW, CommandTestUtil.DESC_AMY_REVIEW);

        // same values -> returns true
        REditCommand.EditReviewDescriptor copyDescriptor =
                new REditCommand.EditReviewDescriptor(CommandTestUtil.DESC_AMY_REVIEW);
        REditCommand commandWithSameValues = new REditCommand(TypicalIndexes.INDEX_FIRST_REVIEW, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(
                new REditCommand(TypicalIndexes.INDEX_SECOND_REVIEW, CommandTestUtil.DESC_AMY_REVIEW)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(
                new REditCommand(TypicalIndexes.INDEX_FIRST_REVIEW, CommandTestUtil.DESC_BOB_REVIEW)));
    }
     */

}
