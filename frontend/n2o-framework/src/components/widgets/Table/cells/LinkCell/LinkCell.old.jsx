import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import get from 'lodash/get'
import Button from 'reactstrap/lib/Button'
import { compose, setDisplayName } from 'recompose'
import { Link } from 'react-router-dom'

import withCell from '../../withCell'
import { LinkType } from '../../../../../impl/linkTypes'
import Toolbar from '../../../../buttons/Toolbar'

import { LinkCellType } from './linkCellTypes'

/**
 * Компонент LinkCell
 * @param id
 * @param fieldKey
 * @param className - класс
 * @param style - объект стилей
 * @param model - модель
 * @param callActionImpl - экшен на клик
 * @param icon - класс иконки
 * @param type
 * @param url - при наличии этого параметра, в зависимости от
 * параметра target будет создана ссылка с соответствующим таргетом,
 * при отсутствии, компонент будет вызывать приходящий экшен
 * @param target - тип ссылки
 * @param visible - флаг видимости
 * @returns {*}
 * @constructor
 */
function LinkCell({
    id,
    fieldKey,
    className,
    style,
    model,
    callActionImpl,
    icon,
    type,
    url,
    target,
    visible,
}) {
    const props = {
        style,
        className: classNames('n2o-link-cell', 'p-0', { [className]: className }),
    }

    const handleClick = (e) => {
        callActionImpl(e, {})
    }

    const getLinkContent = () => (
        <>
            {icon &&
          (type === LinkCellType.ICON || type === LinkCellType.ICONANDTEXT) && (
          <i style={{ marginRight: 5 }} className={icon} />
            )}
            {(type === LinkCellType.ICONANDTEXT || type === LinkCellType.TEXT) &&
          get(model, fieldKey || id)}
        </>
    )

    return (
        visible &&
    (url ? (
        <>
            {target === LinkType.APPLICATION && (
                <Link to={url} {...props} onClick={handleClick}>
                    {getLinkContent()}
                </Link>
            )}
            {(target === LinkType.SELF || target === LinkType.BLANK) && (
                <a
                    onClick={e => e.stopPropagation()}
                    href={url}
                    target={target === LinkType.BLANK ? '_blank' : ''}
                    {...props}
                >
                    {getLinkContent()}
                </a>
            )}
        </>
    ) : (
        <Button color="link" onClick={handleClick} {...props}>
            {getLinkContent()}
        </Button>
    ))
    )
}

LinkCell.propTypes = {
    /**
     * Иконка
     */
    icon: PropTypes.string,
    /**
     * Тип ячейки
     */
    type: PropTypes.string,
    /**
     * ID ячейки
     */
    id: PropTypes.string,
    /**
     * Ключ значения из модели
     */
    fieldKey: PropTypes.string,
    /**
     * Модель данных
     */
    model: PropTypes.object,
    /**
     * Флаг видимости
     */
    visible: PropTypes.bool,
    /**
     * При наличии этого параметра, в зависимости от
     * параметра target будет создана ссылка с соответствующим таргетом,
     * при отсутствии, компонент будет вызывать приходящий экшен
     */
    url: PropTypes.string,
    /**
     * Тип ссылки
     */
    target: PropTypes.string,
}

LinkCell.defaultProps = {
    type: LinkCellType.TEXT,
    visible: true,
}

export { LinkCell }
export default compose(
    setDisplayName('LinkCell'),
    withCell,
)(LinkCell)
