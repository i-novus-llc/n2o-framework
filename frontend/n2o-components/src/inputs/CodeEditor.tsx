import React from 'react'
import AceEditor from 'react-ace'
import cx from 'classnames'
import 'brace/mode/java'
import 'brace/mode/groovy'
import 'brace/mode/html'
import 'brace/mode/sql'
import 'brace/mode/javascript'
import 'brace/mode/xml'
import 'brace/theme/tomorrow'
import 'brace/ext/language_tools'

import { TBaseInputProps, TBaseProps } from '../types'

import '../styles/controls/CodeEditor.scss'

type CodeEditorProps = TBaseProps & TBaseInputProps<string> & {
    lang?: 'javascript' | 'xml' | 'sql' | 'groovy' | 'java' | 'html',
    maxLines?: number,
    minLines?: number,
}

export const CodeEditor = ({
    disabled,
    name,
    visible,
    value,
    autocomplete,
    className,
    onChange,
    minLines = 5,
    maxLines = 100,
    lang = 'javascript',
}: CodeEditorProps) => {
    if (!visible) {
        return null
    }

    return (
        <div
            className={cx('n2o-code-editor', className)}
        >
            <AceEditor
                style={{ resize: 'horizontal' }}
                mode={lang}
                theme="tomorrow"
                name={name}
                onChange={onChange}
                fontSize={14}
                showPrintMargin
                showGutter
                readOnly={disabled}
                minLines={minLines}
                maxLines={maxLines}
                highlightActiveLine
                value={value}
                enableBasicAutocompletion={autocomplete}
                setOptions={{
                    showLineNumbers: true,
                    tabSize: 2,
                }}
            />
        </div>
    )
}

CodeEditor.defaultProps = {
    autocomplete: false,
    lang: 'javascript',
    onChange: () => {},
    disabled: false,
    visible: true,
    maxLines: 100,
} as CodeEditorProps
