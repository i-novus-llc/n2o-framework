import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { pick, isEmpty } from 'lodash';
import { Button, UncontrolledTooltip } from 'reactstrap';
import Icon from '../../../../snippets/Icon/Icon';
import { MODIFIERS, initUid } from './until';
import SecurityCheck from '../../../../../core/auth/SecurityCheck';

/**
 * Кнопка с тултипом
 * @param title - Название кнопки
 * @param hint - Тултип кнопки
 * @param visible - Видимость кнопки
 * @param uId - уникальный идентификатор
 * @param icon - Иконка кнопки
 * @param action - Переданное действие
 * @param onClick - функция обработки клика
 * @param security - объект настройки прав
 * @param rest - остальные props
 * @returns {*}
 * @constructor
 */
function HintButton({ uId, title, hint, visible, icon, onClick, action, security, ...rest }) {
  const otherBtnProps = pick(rest, ['size', 'active', 'color', 'disabled']);
  const otherToltipProps = pick(rest, ['delay', 'placement', 'hideArrow', 'offset']);

  const handleClick = action => e => {
    e.stopPropagation();
    onClick(e, action);
  };

  const render = () =>
    visible ? (
      <Fragment>
        <Button id={uId} onClick={handleClick(action)} {...otherBtnProps}>
          {icon && <Icon name={icon} />}
          {title}
        </Button>
        {hint && (
          <UncontrolledTooltip target={uId} {...otherToltipProps} modifiers={MODIFIERS}>
            {hint}
          </UncontrolledTooltip>
        )}
      </Fragment>
    ) : null;

  return isEmpty(security) ? (
    render()
  ) : (
    <SecurityCheck
      config={security}
      render={({ permissions }) => {
        return permissions ? render() : null;
      }}
    />
  );
}

HintButton.propTypes = {
  title: PropTypes.string,
  hint: PropTypes.string,
  visible: PropTypes.bool,
  disabled: PropTypes.bool,
  id: PropTypes.string,
  size: PropTypes.string,
  color: PropTypes.string,
  placement: PropTypes.oneOf([
    'auto',
    'auto-start',
    'auto-end',
    'top',
    'top-start',
    'top-end',
    'right',
    'right-start',
    'right-end',
    'bottom',
    'bottom-start',
    'bottom-end',
    'left',
    'left-start',
    'left-end'
  ]),
  delay: PropTypes.oneOfType([
    PropTypes.shape({ show: PropTypes.number, hide: PropTypes.number }),
    PropTypes.number
  ]),
  hideArrow: PropTypes.bool,
  offset: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};

HintButton.defaultProps = {
  visible: true,
  disabled: false,
  size: 'sm',
  color: 'link',
  placement: 'top',
  delay: 100,
  hideArrow: false,
  offset: 0
};

export default initUid(HintButton);
