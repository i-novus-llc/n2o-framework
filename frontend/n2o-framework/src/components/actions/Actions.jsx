import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import ButtonToolbar from 'reactstrap/lib/ButtonToolbar'
import ButtonGroup from 'reactstrap/lib/ButtonGroup'
import Button from 'reactstrap/lib/Button'
import DropdownMenu from 'reactstrap/lib/DropdownMenu'
import DropdownItem from 'reactstrap/lib/DropdownItem'
import { compose, setDisplayName } from 'recompose'
import get from 'lodash/get'

import { callActionImpl } from '../../ducks/toolbar/store'
import { resolveWidget } from '../../ducks/widgets/store'
import { PopoverConfirm } from '../snippets/PopoverConfirm/PopoverConfirm'
import SecurityNotRender from '../../core/auth/SecurityNotRender'
import linkResolver from '../../utils/linkResolver'

import ButtonContainer from './ButtonContainer'
import ModalDialog from './ModalDialog/ModalDialog'

const ConfirmMode = {
    POPOVER: 'popover',
    MODAL: 'modal',
}

/**
 * @deprecated
 */

/**
 * Компонент redux-обертка для тулбара
 * @reactProps {object} actions - объект с src экшенов
 * @reactProps {object} toolbar - массив из групп кнопок
 * @reactProps {string} entityKey - id контейнера (widgetId, pageId...)
 * @reactProps {function} resolve
 * @reactProps {object} options
 * @reactProps {string} className
 * @reactProps {object} style
 * @example
 * const actions =  {
 *  "update": {
 *      "src": "dummyActionImpl"
 *  },
 *  "delete": {
 *      "src": "dummyActionImpl"
 *  }
 *}
 *
 * const toolbar = [
 *      {
 *        buttons: [
 *          {
 *            id: '1',
 *            title: "Кнопка",
 *            actionId: 'dummy',
 *            hint: "Кликни меня",
 *          },
 *          {
 *            id: '2',
 *            title: "Click2",
 *            hint: "Click Click Click",
 *            subMenu: [
 *              {
 *                id: '3',
 *                title: "Click3",
 *                actionId: 'dummy',
 *                hint: "Click Click Click",
 *              },
 *              {
 *                id: '4',
 *                title: "Click4",
 *                hint: "Click Click Click",
 *                actionId: 'dummy'
 *              }
 *            ]
 *          }
 *        ]
 *      }
 *    ]
 *
 *   <Actions toolbar={toolbar} actions={actions} containerKey="FormWidget"/>
 *
 */
class Actions extends React.Component {
    /* eslint-disable react/no-array-index-key */
    constructor(props) {
        super(props)
        this.state = {
            confirmVisibleId: null,
        }
        this.closeConfirm = this.closeConfirm.bind(this)
        this.onClickHelper = this.onClickHelper.bind(this)
        this.mapButtonConfirmProps = this.mapButtonConfirmProps.bind(this)
    }

    /**
     * Закрывает окноподтверждаения
     */
    closeConfirm() {
        this.setState({ confirmVisibleId: null })
    }

    /**
     * Обертка вокруг onClick
     * @param button
     * @param confirm
     */
    onClickHelper(button, confirm) {
        const {
            actions,
            resolve,
            options,
            resolveBeforeAction,
            model,
            resolveWidget,
        } = this.props

        if (resolveBeforeAction) {
            resolveWidget(resolveBeforeAction, model)
        }

        this.onClick(
            button.actionId,
            button.id,
            confirm,
            actions,
            resolve,
            button.validatedWidgetId,
            button.validate,
            options,
        )
    }

    /**
     * Маппинг свойст модального окна подтверждения
     * @param confirm
     * @returns {{text: *} | void}
     */
    // eslint-disable-next-line consistent-return
    mapButtonConfirmProps({ confirm }) {
        if (confirm) {
            const { store } = this.context
            const state = store.getState()
            const { modelLink, text } = confirm
            const resolvedText = linkResolver(state, {
                link: modelLink,
                value: text,
            })

            return {
                ...confirm,
                text: resolvedText,
            }
        }
    }

    /**
   * рендер кнопки или элемента списка дропдауна
   * @param Component
   * @param button
   * @param parentId
   * @returns {*}
   */
    renderButton(Component, button, parentId) {
        const { confirmVisibleId } = this.state
        const isConfirmVisible = confirmVisibleId === button.id
        const onConfirm = () => {
            this.onClickHelper(button)
            this.closeConfirm()
        }
        const { containerKey } = this.props
        const Container = (
            <ButtonContainer
                id={button.id}
                onClick={() => this.onClickHelper(button, button.confirm)}
                initialProps={button}
                component={Component}
                containerKey={containerKey}
                parentId={parentId}
            />
        )
        const confirmMode = get(button, 'confirm.mode', ConfirmMode.MODAL)

        let buttonContent = Container

        if (confirmMode === ConfirmMode.POPOVER) {
            buttonContent = (
                <PopoverConfirm
                    {...this.mapButtonConfirmProps(button)}
                    isOpen={isConfirmVisible}
                    onConfirm={onConfirm}
                    onDeny={this.closeConfirm}
                >
                    {Container}
                </PopoverConfirm>
            )
        } else if (confirmMode === ConfirmMode.MODAL) {
            buttonContent = (
                <>
                    {Container}
                    <ModalDialog
                        {...this.mapButtonConfirmProps(button)}
                        visible={isConfirmVisible}
                        onConfirm={onConfirm}
                        onDeny={this.closeConfirm}
                        close={this.closeConfirm}
                    />
                </>
            )
        }
        const btn = <>{buttonContent}</>

        return <SecurityNotRender config={button.security} component={btn} />
    }

    /**
     * Корневой рендер кнопок
     * @param buttons
     * @returns {*}
     */
    renderButtons(buttons) {
        return (
            buttons &&
      buttons.map((button, index) => {
          let buttonEl = null

          if (button.subMenu) {
              buttonEl = this.renderDropdownButton(button)
          } else if (button.dropdownSrc) {
              buttonEl = this.renderCustomDropdown(button)
          } else {
              buttonEl = this.renderButton(Button, button)
          }

          return (
              <SecurityNotRender
                  key={index}
                  config={button.security}
                  component={buttonEl}
              />
          )
      })
        )
    }

    /**
     * резолв экшена
     */
    onClick(
        actionId,
        id,
        confirm,
        actions,
        resolve,
        validatedWidgetId,
        validate = true,
        options = {},
    ) {
        if (confirm) {
            this.setState({ confirmVisibleId: id })
        } else {
            const { pageId } = this.props

            resolve(actions[actionId].src, validatedWidgetId, {
                ...actions[actionId].options,
                actionId,
                buttonId: id,
                validate,
                pageId,
                ...options[actionId],
            })
        }
    }

    /**
   * рендер кнопки-дропдауна
   */
    renderDropdownButton({
        title,
        color,
        id,
        hint,
        visible,
        hintPosition,
        subMenu,
        icon,
        size,
        disabled,
    }) {
        const dropdownProps = {
            size,
            title,
            color,
            hint,
            icon,
            visible,
            disabled,
            hintPosition,
        }
        const { containerKey } = this.props

        return (
            <ButtonContainer
                id={id}
                component={DropdownMenu}
                initialProps={dropdownProps}
                containerKey={containerKey}
                color={color}
            >
                {subMenu.map(item => this.renderButton(DropdownItem, item, id))}
            </ButtonContainer>
        )
    }

    /**
   * Рендер дропдауна с кастомным меню (по dropdownSrc)
   * @param title
   * @param color
   * @param id
   * @param hint
   * @param hintPosition
   * @param visible
   * @param subMenu
   * @param dropdownSrc
   * @param icon
   * @param size
   * @returns {*}
   */
    renderCustomDropdown({
        title,
        color,
        id,
        hint,
        hintPosition,
        visible,
        dropdownSrc,
        icon,
        size,
    }) {
        const { containerKey } = this.props
        const { resolveProps } = this.context
        const CustomMenu = resolveProps(dropdownSrc)
        const dropdownProps = {
            size,
            title,
            color,
            hint,
            hintPosition,
            visible,
            icon,
        }

        return (
            <ButtonContainer
                id={id}
                component={DropdownMenu}
                containerKey={containerKey}
                initialProps={dropdownProps}
            >
                <CustomMenu widgetId={containerKey} />
            </ButtonContainer>
        )
    }

    /**
   * Базовый рендер
   */
    render() {
        const { toolbar, className, style } = this.props

        return (
            <ButtonToolbar className={className} style={style}>
                {toolbar.map(({ buttons, style, className, security }, i) => {
                    const buttonGroup = (
                        <ButtonGroup style={style} className={className}>
                            {this.renderButtons(buttons)}
                        </ButtonGroup>
                    )

                    return (
                        <SecurityNotRender
                            key={i}
                            config={security}
                            component={buttonGroup}
                        />
                    )
                })}
            </ButtonToolbar>
        )
    }
}

Actions.contextTypes = {
    store: PropTypes.object,
    resolveProps: PropTypes.func,
}

Actions.defaultProps = {
    toolbar: [],
    resolveBeforeAction: false,
}

Actions.propTypes = {
    /**
   * Настройка рендера кнопок
   */
    toolbar: PropTypes.array,
    /**
   * Объект экшенов
   */
    actions: PropTypes.object,
    /**
   * Id контейнера
   */
    containerKey: PropTypes.string,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Стили
   */
    style: PropTypes.object,
    /**
   * Функция вызова резолва экшена
   */
    resolve: PropTypes.func,
    /**
   * Доболнительные параметры экшенов
   */
    options: PropTypes.object,
    /**
   * Параметр резолва модели перед выполнением экшена
   */
    resolveBeforeAction: PropTypes.oneOfType([PropTypes.string, PropTypes.bool]),
    /**
   * Модель резолва
   */
    model: PropTypes.object,
    pageId: PropTypes.string,
    resolveWidget: PropTypes.func,
}

/**
 * мэппинг диспатча экшенов в функции
 * @param dispatch
 */
const mapDispatchToProps = dispatch => ({
    resolve: (actionSrc, validatedWidgetId, options) => {
        dispatch(
            callActionImpl(actionSrc, { ...options, dispatch, validatedWidgetId }),
        )
    },
    resolveWidget: (widgetId, model) => dispatch(resolveWidget(widgetId, model)),
})

export { Actions }
export default compose(
    setDisplayName('Actions'),
    connect(
        null,
        mapDispatchToProps,
    ),
)(Actions)
