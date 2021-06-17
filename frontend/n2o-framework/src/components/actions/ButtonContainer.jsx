import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import { createStructuredSelector } from 'reselect'
import cx from 'classnames'

import { registerButton, removeButtons } from '../../actions/toolbar'
import {
    isDisabledSelector,
    isInitSelector,
    isVisibleSelector,
    titleSelector,
    countSelector,
    sizeSelector,
    colorSelector,
    iconSelector,
    classSelector,
    hintSelector,
    hintPositionSelector,
    styleSelector,
} from '../../selectors/toolbar'
import withTooltip from '../../utils/withTooltip'
import { id } from '../../utils/id'

import Dropdown from './Dropdowns/Dropdown'

/**
 * кнопка-контейнер
 */
class ButtonContainer extends React.Component {
    /**
     * Диспатч экшена регистрации виджета
     */
    static initIfNeeded(props) {
        const {
            isInit,
            dispatch,
            id,
            parentId,
            containerKey,
            initialProps: {
                visible = true,
                disabled = false,
                size,
                color,
                count,
                title,
                icon,
                hint,
                className,
                style,
                conditions,
                resolveEnabled,
                hintPosition,
            },
        } = props

        if (!isInit) {
            dispatch(
                registerButton(containerKey, id, {
                    id,
                    visible,
                    disabled,
                    size,
                    parentId,
                    color,
                    icon,
                    count,
                    title,
                    hint,
                    className,
                    style,
                    conditions,
                    containerKey,
                    resolveEnabled,
                    hintPosition,
                }),
            )
        }
    }

    static getDerivedStateFromProps(props) {
        ButtonContainer.initIfNeeded(props)

        return null
    }

    constructor(props) {
        super(props)
        this.state = {}
        this.buttonId = id()
        this.onClick = this.onClick.bind(this)
    }

    componentWillUnmount() {
        const { dispatch, containerKey } = this.props

        dispatch(removeButtons(containerKey))
    }

    onClick(e) {
        const { onClick } = this.props

        e.stopPropagation()
        onClick()
    }

    /**
   * рендер кнопки или элемента списка
   * @returns {*}
   */
    renderButton() {
        const {
            count,
            icon,
            className,
            disabled,
            size,
            color,
            title,
            component,
        } = this.props

        const counter =
      (count || '') && count > 0 ? (
          <span
              className={cx('badge', 'ml-1', {
                  'badge-light': color !== 'secondary',
                  'badge-dark': color === 'secondary',
              })}
          >
              {count}
          </span>
      ) : (
          ''
      )

        return React.createElement(
            component,
            {
                key: this.buttonId,
                id: this.buttonId,
                onClick: this.onClick,
                disabled,
                size,
                color,
                className,
            },
            <>
                <span className={cx({ 'mr-1': title })}>
                    <i className={icon} />
                </span>
                {title}
                {' '}
                {counter}
            </>,
        )
    }

    /**
   * рендер кнопки дропдауна
   * @returns {*}
   */
    renderDropdown() {
        const { children, icon, color, size, title, disabled } = this.props

        return (
            <Dropdown
                id={this.buttonId}
                disabled={disabled}
                color={color}
                size={size}
                title={(
                    <span>
                        <i className={icon} />
                        {' '}
                        {title}
                    </span>
                )}
            >
                {children}
            </Dropdown>
        )
    }

    /**
   *Базовый рендер
   */
    render() {
        const {
            visible,
            hint,
            hintPosition,
            component,
        } = this.props
        const isDropdown = component === DropdownMenu

        if (isDropdown) {
            return (
                <div
                    className={cx(visible ? 'd-block' : 'd-none')}
                    onClick={e => e.stopPropagation()}
                >
                    {withTooltip(this.renderDropdown(), hint, hintPosition, this.buttonId)}
                </div>
            )
        }
        if (visible) {
            return withTooltip(this.renderButton(), hint, hintPosition, this.buttonId)
        }

        return null
    }
}

const mapStateToProps = createStructuredSelector({
    isInit: (state, ownProps) => isInitSelector(ownProps.containerKey, ownProps.id)(state),
    visible: (state, ownProps) => isVisibleSelector(ownProps.containerKey, ownProps.id)(state),
    disabled: (state, ownProps) => isDisabledSelector(ownProps.containerKey, ownProps.id)(state),
    color: (state, ownProps) => colorSelector(ownProps.containerKey, ownProps.id)(state),
    size: (state, ownProps) => sizeSelector(ownProps.containerKey, ownProps.id)(state),
    title: (state, ownProps) => titleSelector(ownProps.containerKey, ownProps.id)(state),
    count: (state, ownProps) => countSelector(ownProps.containerKey, ownProps.id)(state),
    icon: (state, ownProps) => iconSelector(ownProps.containerKey, ownProps.id)(state),
    hint: (state, ownProps) => hintSelector(ownProps.containerKey, ownProps.id)(state),
    hintPosition: (state, ownProps) => hintPositionSelector(ownProps.containerKey, ownProps.id)(state),
    className: (state, ownProps) => classSelector(ownProps.containerKey, ownProps.id)(state),
    style: (state, ownProps) => styleSelector(ownProps.containerKey, ownProps.id)(state),
})

ButtonContainer.propTypes = {
    // eslint-disable-next-line react/no-unused-prop-types
    isInit: PropTypes.bool,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
    color: PropTypes.string,
    size: PropTypes.string,
    title: PropTypes.string,
    count: PropTypes.number,
    icon: PropTypes.string,
    hint: PropTypes.string,
    hintPosition: PropTypes.string,
    className: PropTypes.string,
    // eslint-disable-next-line react/no-unused-prop-types
    style: PropTypes.object,
    dispatch: PropTypes.func,
    containerKey: PropTypes.string,
    component: PropTypes.func,
    onClick: PropTypes.func,
    children: PropTypes.any,
}

ButtonContainer.defaultProps = {
    visible: true,
    disabled: false,
}

export default connect(mapStateToProps)(ButtonContainer)
