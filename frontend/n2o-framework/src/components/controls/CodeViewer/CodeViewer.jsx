import React from 'react';
import compose from 'recompose/compose';
import withState from 'recompose/withState';
import withHandlers from 'recompose/withHandlers';
import PropTypes from 'prop-types';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { prism, darcula } from 'react-syntax-highlighter/dist/esm/styles/prism';
import cx from 'classnames';
import { CopyToClipboard } from 'react-copy-to-clipboard';

/**
 * Компонент CodeViewer
 * @reactProps {boolean} visible - отображается / не отображается
 * @reactProps {string} children - код для отображения
 * @reactProps {string} language - язык программирования
 * @reactProps {string} theme - цвет фона
 * @reactProps {boolean} showLineNumbers - отображать нумерацию строк
 * @reactProps {number} startingLineNumber - если включен showLineNumbers, нумерация строк начнется c указанного числа
 * @reactProps {string} title - закголовок CodeViewer
 * @reactProps {string} text - описание CodeViewer
 */

const colorTheme = {
  LIGHT: 'light',
  DARK: 'dark',
};

function CodeViewer({
  visible,
  children,
  theme,
  copied,
  handleCopy,
  handleShow,
  show,
  title,
  text,
  ...rest
}) {
  return (
    visible && (
      <div className="n2o-code-viewer">
        <h4>{title}</h4>
        <p>{text}</p>
        <div className="code-viewer-actions">
          <CopyToClipboard text={children} onCopy={handleCopy}>
            <i
              className={cx(
                'code-viewer-actions-copy',
                copied
                  ? 'n2o-icon fa fa-check text-success'
                  : 'n2o-icon fa fa-clipboard'
              )}
            />
          </CopyToClipboard>
          <i
            className="n2o-icon code-viewer-actions-code fa fa-code"
            onClick={handleShow}
          />
        </div>
        {show ? (
          <SyntaxHighlighter
            style={theme === colorTheme.DARK ? darcula : prism}
            {...rest}
          >
            {children}
          </SyntaxHighlighter>
        ) : null}
      </div>
    )
  );
}

CodeViewer.propTypes = {
  /**
   * Флаг видимости
   */
  visible: PropTypes.bool,
  /**
   * Код для отображения
   */
  children: PropTypes.string,
  /**
   * Язык кода
   */
  language: PropTypes.string,
  /**
   * Цвет фона
   */
  theme: PropTypes.string,
  /**
   * Отображать нумерацию строк
   */
  showLineNumbers: PropTypes.bool,
  /**
   * Если включен showLineNumbers, нумерация строк начнется c указанного числа
   */
  startingLineNumber: PropTypes.number,
  /**
   * Заголовок CodeViewer
   */
  title: PropTypes.string,
  /**
   * Описание CodeViewer
   */
  text: PropTypes.string,
};

CodeViewer.defaultProps = {
  visible: true,
  theme: colorTheme.LIGHT,
  title: '',
  text: '',
};

export { CodeViewer };
export default compose(
  withState('show', 'setShow', ({ visible }) => visible),
  withState('copied', 'setCopy', false),
  withHandlers({
    handleCopy: ({ setCopy }) => {
      setCopy(true);
      setTimeout(() => {
        setCopy(false);
      }, 2000);
    },
    handleShow: ({ show, setShow }) => {
      setShow(!show);
    },
  })
)(CodeViewer);
