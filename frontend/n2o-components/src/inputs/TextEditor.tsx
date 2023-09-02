import React, { SyntheticEvent, memo, useState } from 'react'
import cn from 'classnames'
import { EditorState, convertToRaw, ContentState } from 'draft-js'
import { Editor } from 'react-draft-wysiwyg'
import draftToHtml from 'draftjs-to-html'
import htmlToDraft from 'html-to-draftjs'

import { TBaseInputProps, TBaseProps } from '../types'

import '../styles/controls/TextEditor.scss'

type TextEditorProps = TBaseProps & TBaseInputProps<string> & {
    onBlur?(e: SyntheticEvent): void,
    onChange?(value: string): void,
    onFocus?(e: SyntheticEvent): void,
    toolbarConfig?: Record<string, unknown>,
}

const convertToHtml = (editorState: EditorState) => draftToHtml(convertToRaw(editorState.getCurrentContent()))

const convertToEditorState = (value: string) => {
    const contentBlock = htmlToDraft(value)

    if (contentBlock) {
        const contentState = ContentState.createFromBlockArray(
            contentBlock.contentBlocks,
        )

        return EditorState.createWithContent(contentState)
    }

    return EditorState.createEmpty()
}

export const TextEditor = memo(({
    className,
    disabled = false,
    visible = true,
    onFocus,
    onChange,
    onBlur,
    toolbarConfig,
    value = '',
}: TextEditorProps) => {
    const [editorState, setEditorState] = useState<EditorState>(EditorState.moveFocusToEnd(convertToEditorState(value || '')))

    const onEditorStateChange = (editorState: EditorState) => {
        const value = convertToHtml(editorState)

        if (onChange) {
            onChange(value)
        }

        setEditorState(editorState)
    }

    return (
        <div>
            {visible && (
                <Editor
                    onFocus={onFocus}
                    onBlur={onBlur}
                    editorState={editorState}
                    wrapperClassName={cn('n2o-text-editor-wrapper', {
                        'n2o-text-editor-wrapper--disabled': disabled,
                    })}
                    editorClassName={cn('n2o-text-editor', className)}
                    onEditorStateChange={onEditorStateChange}
                    toolbar={toolbarConfig}
                />
            )}
        </div>
    )
})
