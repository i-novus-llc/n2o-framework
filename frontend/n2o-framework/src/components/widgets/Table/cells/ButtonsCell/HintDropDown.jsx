import React from 'react'
import PropTypes from 'prop-types'
import defaultTo from 'lodash/defaultTo'
import pick from 'lodash/pick'
import isEmpty from 'lodash/isEmpty'
import { Button, UncontrolledTooltip } from 'reactstrap'
import { compose, withState } from 'recompose'
import { Manager, Reference } from 'react-popper'

// eslint-disable-next-line import/no-named-as-default
import Icon from '../../../../snippets/Icon/Icon'
import SecurityCheck from '../../../../../core/auth/SecurityCheck'
// eslint-disable-next-line import/no-named-as-default
import DropdownCustomItem from '../../../../snippets/DropdownCustomItem/DropdownCustomItem'

import { MODIFIERS, initUid } from './until'
import HintDropDownBody from './HintDropDownBody'

/**
 * @param label - Название компонента dropdown
 * @param hint - Тултип компонента dropdown
 * @param visible - Видимость компонента dropdown
 * @param uId - уникальный идентификатор
 * @param icon - Иконка компонента dropdown
 * @param menu - Элементы списка
 * @param onClick - функция обработки клика
 * @param security - объект настройки прав
 * @param resolveWidget - функция реззолва
 * @param security - объект настройки прав
 * @param rest - остальные props
 * @returns {*}
 * @constructor
 */
function HintDropDown({
    uId,
    open,
    onToggle,
    title,
    hint,
    visible,
    menu,
    icon,
    onClick,
    security,
    hintPosition,
    positionFixed,
    modifiers,
    resolveWidget,
    model,
    ...rest
}) {
    const otherToltipProps = pick(rest, ['delay', 'hideArrow', 'offset'])
    const dropdownProps = pick(rest, [
        'disabled',
        'direction',
        'active',
        'color',
        'size',
    ])

    const createDropDownMenu = ({
        title,
        visible,
        icon,
        action,
        security,
        color,
        ...itemProps
    }) => {
        const handleClick = action => (e) => {
            e.stopPropagation()
            onClick(e, action)
            onToggleDropdown()
        }

        const renderItem = () => (defaultTo(visible, true) ? (
            <DropdownCustomItem
                color={color}
                onClick={handleClick(action)}
                {...itemProps}
            >
                {icon && <Icon name={icon} />}
                {title}
            </DropdownCustomItem>
        ) : null)

        return isEmpty(security) ? (
            renderItem()
        ) : (
            <SecurityCheck
                config={security}
                render={({ permissions }) => (permissions ? renderItem() : null)}
            />
        )
    }

    const onToggleDropdown = (e) => {
        if (e) { e.stopPropagation() }
        resolveWidget(model)
        onToggle(!open)
    }

    const renderDropdown = () => (visible ? (
        <Manager>
            {hint && (
                <UncontrolledTooltip
                    target={uId}
                    modifiers={MODIFIERS}
                    placement={hintPosition}
                    {...otherToltipProps}
                >
                    {hint}
                </UncontrolledTooltip>
            )}
            <Reference>
                {({ ref }) => (
                    <Button
                        id={uId}
                        className="n2o-buttons-cell-toggler btn btn-link border-0 bg-transparent"
                        innerRef={ref}
                        size="sm"
                        color="light"
                        onClick={onToggleDropdown}
                        {...dropdownProps}
                    >
                        {icon && <Icon name={icon} />}
                        {title}
                        <Icon className="n2o-dropdown-icon" name="fa fa-angle-down" />
                    </Button>
                )}
            </Reference>
            <HintDropDownBody
                modifiers={modifiers}
                positionFixed={positionFixed}
                menu={menu}
                onToggleDropdown={onToggleDropdown}
                createDropDownMenu={createDropDownMenu}
                open={open}
            />
        </Manager>
    ) : null)

    return isEmpty(security) ? (
        renderDropdown()
    ) : (
        <SecurityCheck
            config={security}
            render={({ permissions }) => (permissions ? renderDropdown() : null)}
        />
    )
}

HintDropDown.propTypes = {
    title: PropTypes.string,
    hint: PropTypes.string,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    id: PropTypes.string,
    size: PropTypes.string,
    color: PropTypes.string,
    menu: PropTypes.array,
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
        'left-end',
    ]),
    delay: PropTypes.oneOfType([
        PropTypes.shape({ show: PropTypes.number, hide: PropTypes.number }),
        PropTypes.number,
    ]),
    hideArrow: PropTypes.bool,
    offset: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    positionFixed: PropTypes.bool,
    modifiers: PropTypes.object,
    resolveWidget: PropTypes.func,
    uId: PropTypes.string,
    open: PropTypes.bool,
    onToggle: PropTypes.func,
    icon: PropTypes.string,
    onClick: PropTypes.func,
    security: PropTypes.object,
    hintPosition: PropTypes.string,
    model: PropTypes.object,
    action: PropTypes.object,
}

HintDropDown.defaultProps = {
    visible: true,
    disabled: false,
    placement: 'top',
    size: 'sm',
    color: 'link',
    menu: [],
    delay: 100,
    hideArrow: false,
    offset: 0,
    positionFixed: true,
    modifiers: MODIFIERS,
    resolveWidget: () => {},
}

export default compose(
    withState('open', 'onToggle', false),
    initUid,
)(HintDropDown)
