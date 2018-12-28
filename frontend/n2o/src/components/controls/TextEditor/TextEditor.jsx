import React from 'react';
import PropTypes from 'prop-types';
import RichTextEditor from 'react-rte';

/**
 * Компонент текст-эдитор
 * @reactProps {string} value - начальное значение текста внутри эдитора
 * @reactProps {function} onChange - вызывается при изменении значеня поля
 * @reactProps {boolean} disabled - задизейблен / нет
 * @reactProps {string} name - имя поля
 * @reactProps {boolean} visible
 * @reactProps {string} className - css-класс - здесь можно задать высоту
 * @reactProps {object} toolbarConfig - концфигурация тулбара
 * @see https://github.com/sstur/react-rte
 * @example
 * // пример toolbarConfig
 * const toolbarConfig = {
 *   // Optionally specify the groups to display (displayed in the order listed).
 *   display: ['INLINE_STYLE_BUTTONS', 'BLOCK_TYPE_BUTTONS', 'LINK_BUTTONS', 'BLOCK_TYPE_DROPDOWN', 'HISTORY_BUTTONS'],
 *   INLINE_STYLE_BUTTONS: [
 *    {label: 'Bold', style: 'BOLD', className: 'custom-css-class'},
 *    {label: 'Italic', style: 'ITALIC'},
 *    {label: 'Underline', style: 'UNDERLINE'}
 *   ],
 *   BLOCK_TYPE_DROPDOWN: [
 *    {label: 'Normal', style: 'unstyled'},
 *    {label: 'Heading Large', style: 'header-one'},
 *    {label: 'Heading Medium', style: 'header-two'},
 *    {label: 'Heading Small', style: 'header-three'}
 *   ],
 *   BLOCK_TYPE_BUTTONS: [
 *    {label: 'UL', style: 'unordered-list-item'},
 *    {label: 'OL', style: 'ordered-list-item'}
 *   ]
 * };
 *
 *
 */
class TextEditor extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      richValue: RichTextEditor.createValueFromString(props.value, 'html'),
      htmlValue: props.value
    };
  }

  componentWillReceiveProps(newProps) {
    if (newProps.value !== this.state.htmlValue) {
      this.setState({
        richValue: RichTextEditor.createValueFromString(newProps.value, 'html'),
        htmlValue: newProps.value
      });
    }
  }

  onChange(richValue) {
    this.setState({ richValue, htmlValue: richValue.toString('html') }, () => {
      this.props.onChange(this.state.htmlValue);
    });
  }

  render() {
    const { toolbarConfig, disabled, name, visible, className } = this.props;
    const baseStyle = {
      wordBreak: 'break-all',
      wordWrap: 'break-word',
      maxWidth: '100%',
      display: 'flex'
    };
    const disabledStyle = {
      pointerEvents: 'none',
      opacity: '0.4'
    };
    return (
      <div style={disabled ? { ...baseStyle, ...disabledStyle } : baseStyle}>
        {visible && (
          <RichTextEditor
            value={this.state.richValue}
            name={name}
            editorClassName={`editor ${className}`}
            toolbarConfig={toolbarConfig}
            onChange={this.onChange.bind(this)}
          />
        )}
      </div>
    );
  }
}

TextEditor.propTypes = {
  value: PropTypes.string,
  onChange: PropTypes.func,
  disabled: PropTypes.bool,
  name: PropTypes.string,
  visible: PropTypes.bool,
  className: PropTypes.string,
  toolbarConfig: PropTypes.object
};

TextEditor.defaultProps = {
  onChange: () => {},
  disabled: false,
  visible: true,
  value: ''
};

export default TextEditor;
