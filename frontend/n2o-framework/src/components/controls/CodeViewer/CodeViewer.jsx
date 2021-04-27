import React from 'react'
import compose from 'recompose/compose'
import withState from 'recompose/withState'
import withHandlers from 'recompose/withHandlers'
import PropTypes from 'prop-types'
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter'
import { tomorrow, coy } from 'react-syntax-highlighter/dist/cjs/styles/prism'
import cx from 'classnames'
import { CopyToClipboard } from 'react-copy-to-clipboard'

/**
 * Компонент CodeViewer
 * @reactProps {boolean} visible - отображается / не отображается
 * @reactProps {string} value - код для отображения
 * @reactProps {string} language - язык программирования
 * @reactProps {string} darkTheme - темная тема компонента
 * @reactProps {boolean} showLineNumbers - отображать нумерацию строк
 * @reactProps {number} startingLineNumber - если включен showLineNumbers, нумерация строк начнется c указанного числа
 * @reactProps {boolean} hideButtons - скрыть кнопки
 * @reactProps {boolean} hideOverflow - скрыть вертикальную прокрутку
 */

function CodeViewer({
    visible,
    value,
    darkTheme,
    copied,
    handleCopy,
    handleShow,
    show,
    hideOverflow,
    hideButtons,
    ...rest
}) {
    /* eslint-disable jsx-a11y/click-events-have-key-events */
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
                        {/* eslint-disable-next-line jsx-a11y/no-static-element-interactions */}
                        <i
                            className="n2o-icon code-viewer-actions-code fa fa-code"
                            onClick={handleShow}
                        />
                    </div>
                ) : null}
                {show ? (
                    <SyntaxHighlighter
                        className={!hideOverflow ? 'code-viewer-body' : null}
                        style={darkTheme ? tomorrow : coy}
                        {...rest}
                    >
                        {value}
                    </SyntaxHighlighter>
                ) : null}
            </div>
        )
    )
}

CodeViewer.propTypes = {
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
    /**
   * Код для отображения
   */
    value: PropTypes.string,
    /**
   * Язык кода
   */
    language: PropTypes.string,
    /**
   * Темная тема компонента
   */
    darkTheme: PropTypes.bool,
    /**
   * Отображать нумерацию строк
   */
    showLineNumbers: PropTypes.bool,
    /**
   * Если включен showLineNumbers, нумерация строк начнется c указанного числа
   */
    startingLineNumber: PropTypes.number,
    /**
   * Cкрыть кнопки
   */
    hideButtons: PropTypes.bool,
    /**
   * Скрыть вертикальную прокрутку
   */
    hideOverflow: PropTypes.bool,
}

CodeViewer.defaultProps = {
    visible: true,
    hideButtons: false,
    darkTheme: false,
    hideOverflow: false,
}

export { CodeViewer }
export default compose(
    withState('show', 'setShow', ({ visible }) => visible),
    withState('copied', 'setCopy', false),
    withHandlers({
        handleCopy: ({ setCopy }) => {
            setCopy(true)
            setTimeout(() => {
                setCopy(false)
            }, 2000)
        },
        handleShow: ({ show, setShow }) => {
            setShow(!show)
        },
    }),
)(CodeViewer)
