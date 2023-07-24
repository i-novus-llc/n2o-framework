import React, { useState } from 'react'
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter'
import tomorrow from 'react-syntax-highlighter/dist/cjs/styles/prism/tomorrow'
import coy from 'react-syntax-highlighter/dist/cjs/styles/prism/coy'
import cx from 'classnames'
import { CopyToClipboard } from 'react-copy-to-clipboard'

import { TBaseProps } from '../../types'

type CodeViewerProps = TBaseProps & {
    darkTheme?: boolean,
    hideButtons?: boolean,
    hideOverflow?: boolean,
    language?: string,
    showLineNumbers?: boolean,
    startingLineNumber?: number,
    value: string,
}

export const CodeViewer = ({
    visible = true,
    value,
    language = 'javascript',
    hideOverflow = false,
    hideButtons = false,
    darkTheme = false,
    startingLineNumber,
    showLineNumbers,
    ...rest
}: CodeViewerProps) => {
    const [show, setShow] = useState(true)
    const [copied, setCopied] = useState(false)

    const handleShow = () => {
        setShow(!show)
    }

    const handleCopy = () => {
        setCopied(true)

        setTimeout(() => {
            setCopied(false)
        }, 2000)
    }

    const isDarkTheme = darkTheme ? tomorrow : coy

    return (
        visible && (
            <div className="n2o-code-viewer">
                {!hideButtons ? (
                    <div className="code-viewer-actions">
                        <CopyToClipboard text={value} onCopy={handleCopy}>
                            <i
                                className={cx(
                                    'code-viewer-actions-copy',
                                    copied
                                        ? 'n2o-icon fa fa-check text-success'
                                        : 'n2o-icon fa fa-clipboard',
                                )}
                            />
                        </CopyToClipboard>
                        <i
                            className="n2o-icon code-viewer-actions-code fa fa-code"
                            onClick={handleShow}
                        />
                    </div>
                ) : null}
                {show ? (
                    <SyntaxHighlighter
                        {...rest}
                        language={language}
                        startingLineNumber={startingLineNumber}
                        showLineNumbers={showLineNumbers}
                        className={!hideOverflow ? 'code-viewer-body' : null}
                        style={isDarkTheme}
                    >
                        {value}
                    </SyntaxHighlighter>
                ) : null}
            </div>
        )
    )
}
