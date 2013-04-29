package org.idea.plugin.duplicatelines;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;

public class CopyLineUpAction extends EditorAction {

    public CopyLineUpAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public CopyLineUpAction() {
        this(new UpHandler());
    }

    private static class UpHandler extends EditorWriteActionHandler {
        private UpHandler() {
        }

        @Override
        public void executeWriteAction(Editor editor, DataContext dataContext) {
            Document document = editor.getDocument();

            if (editor == null || document == null || !document.isWritable()) {
                return;
            }

            // CaretModel used to find caret position
            CaretModel caretModel = editor.getCaretModel();
            // SelectionModel used to find selection ranges
            SelectionModel selectionModel = editor.getSelectionModel();

            // get the range of the selected characters
            Range charsRange = new Range(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd());
            // get the range of the selected lines (block of code)
            Range linesRange = new Range(document.getLineNumber(charsRange.getStart()), document.getLineNumber(charsRange.getEnd()));
            // range of the duplicated string
            Range linesBlock = new Range(document.getLineStartOffset(linesRange.getStart()), document.getLineEndOffset(linesRange.getEnd()));

            // get the string to duplicate
            String duplicatedString = document.getText().substring(linesBlock.getStart(), linesBlock.getEnd());
            duplicatedString += "\n";

            // insert new duplicated string into the document
            document.insertString(linesBlock.getStart(), duplicatedString);

            // select duplicated block
            editor.getSelectionModel().setSelection(linesBlock.getStart(), linesBlock.getEnd());
            // move cursor to the start of copied block
            caretModel.moveToOffset(linesBlock.getStart());
            editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
        }
    }
}
