package org.idea.plugin.duplicatelines;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

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
            TextRange charsRange = new TextRange(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd());
            // get the range of the selected lines (block of code)
            TextRange linesRange = new TextRange(document.getLineNumber(charsRange.getStartOffset()), document.getLineNumber(charsRange.getEndOffset()));
            // range of the duplicated string
            TextRange linesBlock = new TextRange(document.getLineStartOffset(linesRange.getStartOffset()), document.getLineEndOffset(linesRange.getEndOffset()));

            // get the string to duplicate
            String duplicatedString = document.getText().substring(linesBlock.getStartOffset(), linesBlock.getEndOffset());
            duplicatedString += "\n";

            // insert new duplicated string into the document
            document.insertString(linesBlock.getStartOffset(), duplicatedString);

            // select duplicated block
            editor.getSelectionModel().setSelection(linesBlock.getStartOffset(), linesBlock.getStartOffset());
            // move cursor to the start of copied block
            caretModel.moveToOffset(linesBlock.getStartOffset());
            editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
        }
    }
}
