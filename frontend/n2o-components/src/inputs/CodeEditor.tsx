import React from 'react'
import AceEditor from 'react-ace'
import classNames from 'classnames'
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

const noop = () => {}

export const CodeEditor = ({
    disabled = false,
    name,
    visible = true,
    value,
    autocomplete = false,
    className,
    onChange = noop,
    minLines = 5,
    maxLines = 100,
    lang = 'javascript',
}: CodeEditorProps) => {
    if (!visible) { return null }

    return (
        <div
            className={classNames('n2o-code-editor', className, { disabled })}
        >
            <AceEditor
                style={!disabled ? { resize: 'vertical' } : {}}
                className="n2o-ace-editor"
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
                value={value || ''}
                enableBasicAutocompletion={autocomplete}
                setOptions={{ showLineNumbers: true, tabSize: 2 }}
                wrapEnabled
            />
        </div>
    )
}
