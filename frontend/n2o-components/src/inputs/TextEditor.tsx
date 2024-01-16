import React, { SyntheticEvent, memo, useState, useEffect } from 'react'
import classNames from 'classnames'
import { EditorState, convertToRaw, ContentState } from 'draft-js'
import { Editor } from 'react-draft-wysiwyg'
import draftToHtml from 'draftjs-to-html'
import htmlToDraft from 'html-to-draftjs'

import { TBaseInputProps, TBaseProps } from '../types'

import '../styles/controls/TextEditor.scss'
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css'

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
    onFocus,
    onChange,
    onBlur,
    toolbarConfig,
    disabled = false,
    visible = true,
    value = '',
}: TextEditorProps) => {
    const defaultState = EditorState.moveFocusToEnd(convertToEditorState(value || ''))
    const [editorState, setEditorState] = useState<EditorState>(defaultState)

    useEffect(() => {
        if (value === null) { setEditorState(defaultState) }
    }, [value])

    if (!visible) { return null }

    const onEditorStateChange = (editorState: EditorState) => {
        const value = convertToHtml(editorState)

        if (onChange) { onChange(value) }
        setEditorState(editorState)
    }

    return (
        <div>
            <Editor
                onFocus={onFocus}
                onBlur={onBlur}
                editorState={editorState}
                wrapperClassName={classNames('n2o-text-editor-wrapper', {
                    'n2o-text-editor-wrapper--disabled': disabled,
                })}
                editorClassName={classNames('n2o-text-editor', className)}
                onEditorStateChange={onEditorStateChange}
                toolbar={toolbarConfig}
                locale="ru"
            />
        </div>
    )
})
