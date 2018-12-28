import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { pick, defaultTo } from 'lodash';
import { Button, UncontrolledTooltip } from 'reactstrap';
import Icon from '../../../../snippets/Icon/Icon';
import { MODIFIERS, initUid } from './until';

/**
 * Кнопка с тултипом
 * @param label - Название кнопки
 * @param hint - Тултип кнопки
 * @param visible - Видимость кнопки
 * @param disabled - Активность кнопки
 * @param id - уникальный идентификатор
 * @param icon - Иконка кнопки
 * @param action - Переданное действие
 * @param placement - Расположение тултипа, по дефолту bottom
 * @param color - цвет кнопки, по дефолту link т.е отображается как ссылка
 * @param active - Кнопка находится в активном состоянии
 * @param size - Размеры кнопки (lg, md, sm). По дефолту md
 * @param delay - Задержка перед показом тултипа
 * @param hideArrow - Показать скрыть стрелку тултипа
 * @param offset - Смещение тултипа относительно положения заданной placement
 * @returns {*}
 * @constructor
 */
function HintButton({ uId, label, hint, visible, id, icon, onClick, action, ...rest }) {
  const otherBtnProps = pick(rest, ['size', 'active', 'color', 'disabled']);
  const otherToltipProps = pick(rest, ['delay', 'placement', 'hideArrow', 'offset']);

  const handlerClick = action => () => onClick(action);

  return (
    visible && (
      <Fragment>
        <Button id={uId} onClick={handlerClick(action)} {...otherBtnProps}>
          {icon && <Icon name={icon} />}
          {label}
        </Button>
        {hint && (
          <UncontrolledTooltip target={uId} {...otherToltipProps} modifiers={MODIFIERS}>
            {hint}
          </UncontrolledTooltip>
        )}
      </Fragment>
    )
  );
}

HintButton.propTypes = {
  label: PropTypes.string,
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
