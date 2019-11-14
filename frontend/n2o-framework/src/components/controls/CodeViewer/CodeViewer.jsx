import React from 'react';
import PropTypes from 'prop-types';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { prism, darcula } from 'react-syntax-highlighter/dist/esm/styles/prism';

/**
 * Компонент CodeViewer
 * @reactProps {boolean} visible - отображается / не отображается
 * @reactProps {string} children - код для отображения
 * @reactProps {string} language - язык программирования
 * @reactProps {string} theme - цвет фона
 * @reactProps {boolean} showLineNumbers - отображать нумерацию строк
 * @reactProps {number} startingLineNumber - если включен showLineNumbers, нумерация строк начнется c указанного числа
 */

const colorTheme = {
  LIGHT: 'light',
  DARK: 'dark',
};

function CodeViewer({ visible, children, theme, ...rest }) {
  return (
    visible && (
      <SyntaxHighlighter
        style={theme === colorTheme.DARK ? darcula : prism}
        {...rest}
      >
        {children}
      </SyntaxHighlighter>
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
};

CodeViewer.defaultProps = {
  visible: true,
  theme: colorTheme.LIGHT,
};

export default CodeViewer;
