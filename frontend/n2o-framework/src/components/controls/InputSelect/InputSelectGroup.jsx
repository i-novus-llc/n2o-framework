import React from 'react';
import PropTypes from 'prop-types';
import InlineSpinner from '../../snippets/Spinner/InlineSpinner';
import Spinner from '../../snippets/Spinner/Spinner';
import InputAddon from './InputAddon';
import cx from 'classnames';

/**
 * InputSelectGroup
 * @reactProps {boolean} loading - флаг анимации загрузки
 * @reactProps {boolean} isExpanded - флаг видимости popUp
 * @reactProps {function} onButtonClick - callback при нажатии на кнопку
 * @reactProps {array} selected - список выбранных элементов
 * @reactProps {node} input
 * @reactProps {node} children - эдемент потомок компонента InputSelectGroup
 * @reactProps {boolean} isInputInFocus
 * @reactProps {boolean} disabled
 * @reactProps {function} onClearClick
 * @reactProps {function} setIsExpanded
 * @reactProps {string} iconFieldId - поле для иконки
 * @reactProps {string} imageFieldId - поле для картинки
 * @reactProps {boolean} cleanable - показывать иконку очисть поле
 * @reactProps {boolean} multiSelect - флаг мульти выбора
 * @reactProps {boolean} withoutButtons - флаг скрытия кнопок действий
 */

function InputSelectGroup({
  className,
  loading,
  isExpanded,
  multiSelect,
  iconFieldId,
  imageFieldId,
  onButtonClick,
  selected,
  input,
  cleanable,
  children,
  isInputInFocus,
  onClearClick,
  setIsExpanded,
  disabled,
  setSelectedItemsRef,
  withoutButtons,
}) {
  const displayAddon =
    !multiSelect && !!selected.length && (iconFieldId || imageFieldId);

  const renderButton = loading => {
    return (
      <Spinner type="inline" loading={loading} size="sm">
        <i
          className={`fa fa-chevron-down`}
          onClick={() => {
            setIsExpanded(!isExpanded);
          }}
          aria-hidden="true"
        />
      </Spinner>
    );
  };
  return (
    <div
      className={cx('n2o-input-container', 'form-control', className, {
        disabled,
      })}
      onClick={onButtonClick}
    >
      <div className="n2o-input-items">
        {displayAddon && (
          <InputAddon
            item={selected[0]}
            imageFieldId={imageFieldId}
            iconFieldId={iconFieldId}
            setRef={setSelectedItemsRef}
          />
        )}
        {children}
      </div>
      {!withoutButtons && !disabled && (
        <div className="n2o-input-control">
          {(selected.length || input) && cleanable && (
            <div
              className={cx('n2o-input-clear', {
                'input-in-focus': isInputInFocus,
              })}
              onClick={onClearClick}
            >
              <i className="fa fa-times" aria-hidden="true" />
            </div>
          )}
          <div className={cx('n2o-popup-control', { isExpanded })}>
            {renderButton(loading)}
          </div>
        </div>
      )}
    </div>
  );
}

InputSelectGroup.propTypes = {
  loading: PropTypes.bool,
  isExpanded: PropTypes.bool.isRequired,
  onButtonClick: PropTypes.func,
  selected: PropTypes.array.isRequired,
  input: PropTypes.node,
  children: PropTypes.node,
  isInputInFocus: PropTypes.node,
  iconFieldId: PropTypes.string,
  imageFieldId: PropTypes.string,
  multiSelect: PropTypes.bool,
  disabled: PropTypes.bool,
  onClearClick: PropTypes.func,
  setIsExpanded: PropTypes.func,
  cleanable: PropTypes.bool,
  withoutButtons: PropTypes.bool,
};

InputSelectGroup.defaultProps = {
  cleanable: true,
  multiSelect: false,
  loading: false,
  collapseSelected: true,
  withoutButtons: false,
  setIsExpanded: () => {},
  onButtonClick: () => {},
};

export default InputSelectGroup;
