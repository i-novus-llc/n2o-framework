package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент, который позволяет задавать текст согласно markdown разметки, а отображать его в виде html.
 */
@Getter
@Setter
public class N2oMarkdown extends N2oField {
    private String content;
    private String[] actionIds;
 }
