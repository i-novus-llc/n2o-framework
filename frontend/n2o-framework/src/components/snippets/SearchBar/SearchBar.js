import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import isString from 'lodash/isString';
import Button from 'reactstrap/lib/Button';
import {
  compose,
  withState,
  withHandlers,
  lifecycle,
  defaultProps,
} from 'recompose';

import InputText from '../../controls/InputText/InputText';

let timeoutId = null;
const ENTER_KEY_CODE = 13;

const SearchTrigger = {
  ENTER: 'ENTER',
  CHANGE: 'CHANGE',
  BUTTON: 'BUTTON',
};

function SearchBar({
  className,
  innerValue,
  icon,
  button,
  onClick,
  onChange,
  onKeyDown,
  placeholder,
}) {
  return (
    <div className={cn('n2o-search-bar', className)}>
      <div className="n2o-search-bar__control">
        <InputText
          onKeyDown={onKeyDown}
          value={innerValue}
          onChange={onChange}
          placeholder={placeholder}
        />
        {isString(icon) ? <i className={icon} /> : icon}
      </div>
      {!!button && (
        <Button {...button} onClick={onClick}>
          {button.label}
          {button.icon && <i className={cn('ml-2', button.icon)} />}
        </Button>
      )}
    </div>
  );
}

SearchBar.propTypes = {
  /**
   * Класс компонента
   */
  className: PropTypes.string,
  /**
   * Значение компонента
   */
  value: PropTypes.string,
  /**
   * Placeholder контрола
   */
  placeholder: PropTypes.string,
  /**
   * Триггер запуска колбека поиска
   */
  trigger: PropTypes.oneOf([
    SearchTrigger.CHANGE,
    SearchTrigger.ENTER,
    SearchTrigger.BUTTON,
  ]),
  /**
   * Настройка кнопки
   */
  button: PropTypes.object,
  /**
   * Иконка
   */
  icon: PropTypes.oneOfType([
    PropTypes.func,
    PropTypes.node,
    PropTypes.element,
    PropTypes.string,
  ]),
  /**
   * Коллбек поиска
   */
  onSearch: PropTypes.func,
  /**
   * Delay поиска при change триггере
   */
  throttleDelay: PropTypes.number,
};

SearchBar.defaultProps = {
  trigger: SearchTrigger.CHANGE,
  button: false,
  icon: 'fa fa-search',
  onSearch: () => {},
};

const enhance = compose(
  defaultProps({
    trigger: SearchTrigger.CHANGE,
    throttleDelay: 400,
  }),
  withState('innerValue', 'setInnerValue', ({ value }) => value),
  withHandlers({
    onClick: ({ innerValue, onSearch }) => () => onSearch(innerValue),
    onKeyDown: ({ innerValue, trigger, onSearch }) => ({ keyCode }) => {
      if (trigger === SearchTrigger.ENTER && keyCode === ENTER_KEY_CODE) {
        onSearch(innerValue);
      }
    },
    onChange: ({
      setInnerValue,
      trigger,
      throttleDelay,
      onSearch,
    }) => value => {
      setInnerValue(value);

      if (trigger === SearchTrigger.CHANGE) {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => onSearch(value), throttleDelay);
      }
    },
  }),
  lifecycle({
    componentDidUpdate(prevProps) {
      if (prevProps.value !== this.props.value) {
        this.setState({ innerValue: this.props.value });
      }
    },
  })
);

export { SearchBar };
export default enhance(SearchBar);
