import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import {
  ButtonToolbar,
  ButtonGroup,
  Button,
  DropdownMenu,
  DropdownItem,
} from 'reactstrap';
import cx from 'classnames';
import { isEmpty, every } from 'lodash';

import { callActionImpl } from '../../actions/toolbar';
import ModalDialog from './ModalDialog/ModalDialog';
import Popover from '../snippets/Popover/Popover';
import factoryResolver from '../../utils/factoryResolver';
import ButtonContainer from './ButtonContainer';

import SecurityNotRender from '../../core/auth/SecurityNotRender';
import linkResolver from '../../utils/linkResolver';

/**
 * Компонент redux-обертка для тулбара
 * @reactProps {object} actions - объект с src экшенов
 * @reactProps {object} toolbar - массив из групп кнопок
 * @reactProps {string} containerKey - id контейнера (widgetId, pageId...)
 * @reactProps {function} resolve
 * @reactProps {object} options
 * @reactProps {string} className
 * @reactProps {object} style
 * @example
 * const actions =  {
 *  "update": {
 *	"src": "dummyActionImpl"
 *  },
 *  "delete": {
 *	"src": "dummyActionImpl"
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
  constructor(props) {
    super(props);
    this.state = {
      confirmVisibleId: null,
    };
    this.closeConfirm = this.closeConfirm.bind(this);
    this.onClickHelper = this.onClickHelper.bind(this);
    this.mapButtonConfirmProps = this.mapButtonConfirmProps.bind(this);
  }

  /**
   * Закрывает окноподтверждаения
   */
  closeConfirm() {
    this.setState({ confirmVisibleId: null });
  }

  /**
   * Обертка вокруг onClick
   * @param button
   * @param confirm
   */
  onClickHelper(button, confirm) {
    const { actions, resolve, options } = this.props;
    this.onClick(
      button.actionId,
      button.id,
      confirm,
      actions,
      resolve,
      button.validatedWidgetId,
      button.validate,
      options
    );
  }

  /**
   * Маппинг свойст модального окна подтверждения
   * @param confirm
   * @returns {{text: *}}
   */
  mapButtonConfirmProps({ confirm }) {
    if (confirm) {
      const store = this.context.store.getState();
      const { modelLink, text } = confirm;
      const resolvedText = linkResolver(store, {
        link: modelLink,
        value: text,
      });
      console.log('text ', resolvedText);
      return {
        ...confirm,
        text: resolvedText,
      };
    }
  }

  /**
   * рендер кнопки или элемента списка дропдауна
   * @param Component
   * @param button
   * @param parentId
   * @param popover
   * @returns {*}
   */
  renderButton(Component, button, parentId, popover = true) {
    const btn = (
      <React.Fragment>
        <ButtonContainer
          id={button.id}
          onClick={
            popover
              ? () => this.setState({ confirmVisibleId: button.id })
              : () => this.onClickHelper(button, button.confirm)
          }
          initialProps={button}
          component={Component}
          containerKey={this.props.containerKey}
          parentId={parentId}
        />
        {!popover && (
          <ModalDialog
            {...this.mapButtonConfirmProps(button)}
            visible={this.state.confirmVisibleId === button.id}
            onConfirm={() => {
              this.onClickHelper(button);
              this.closeConfirm();
            }}
            onDeny={this.closeConfirm}
            close={this.closeConfirm}
          />
        )}
        {popover && (
          <Popover
            {...this.mapButtonConfirmProps(button)}
            target={button.id}
            popConfirm={true}
            onConfirm={() => {
              this.setState({ confirmVisibleId: button.id }, () =>
                this.closeConfirm()
              );
            }}
            onCancel={this.closeConfirm}
          />
        )}
      </React.Fragment>
    );

    return <SecurityNotRender config={button.security} component={btn} />;
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
        let buttonEl = null;
        if (button.subMenu) {
          buttonEl = this.renderDropdownButton(button);
        } else if (button.dropdownSrc) {
          buttonEl = this.renderCustomDropdown(button);
        } else {
          buttonEl = this.renderButton(Button, button);
        }
        return (
          <SecurityNotRender
            key={index}
            config={button.security}
            component={buttonEl}
          />
        );
      })
    );
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
    options = {}
  ) {
    if (confirm) {
      this.setState({ confirmVisibleId: id });
    } else {
      resolve(actions[actionId].src, validatedWidgetId, {
        ...actions[actionId].options,
        actionId,
        buttonId: id,
        validate,
        pageId: this.props.pageId,
        ...options[actionId],
      });
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
    };
    return (
      <ButtonContainer
        id={id}
        component={DropdownMenu}
        initialProps={dropdownProps}
        containerKey={this.props.containerKey}
        color={color}
      >
        {subMenu.map(item => this.renderButton(DropdownItem, item, id))}
      </ButtonContainer>
    );
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
   * @param actionId
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
    subMenu,
    dropdownSrc,
    icon,
    actionId,
    size,
  }) {
    const { containerKey } = this.props;
    const CustomMenu = factoryResolver(dropdownSrc);
    const dropdownProps = {
      size,
      title,
      color,
      hint,
      hintPosition,
      visible,
      icon,
    };
    return (
      <ButtonContainer
        id={id}
        component={DropdownMenu}
        containerKey={this.props.containerKey}
        initialProps={dropdownProps}
      >
        <CustomMenu widgetId={containerKey} />
      </ButtonContainer>
    );
  }

  /**
   * Базовый рендер
   */
  render() {
    const { toolbar, className, style } = this.props;

    return (
      <ButtonToolbar className={className} style={style}>
        {toolbar.map(({ buttons, style, className, security }, i) => {
          const buttonGroup = (
            <ButtonGroup style={style} className={className}>
              {this.renderButtons(buttons)}
            </ButtonGroup>
          );

          return (
            <SecurityNotRender
              key={i}
              config={security}
              component={buttonGroup}
            />
          );
        })}
      </ButtonToolbar>
    );
  }
}

Actions.contextTypes = {
  store: PropTypes.object,
};

Actions.defaultProps = {
  toolbar: [],
};

Actions.propTypes = {
  toolbar: PropTypes.array,
  actions: PropTypes.object,
  containerKey: PropTypes.string,
  className: PropTypes.string,
  style: PropTypes.object,
  resolve: PropTypes.func,
  options: PropTypes.object,
};

/**
 * мэппинг диспатча экшенов в функции
 * @param dispatch
 */
const mapDispatchToProps = dispatch => {
  return {
    resolve: (actionSrc, validatedWidgetId, options) => {
      dispatch(
        callActionImpl(actionSrc, { ...options, dispatch, validatedWidgetId })
      );
    },
  };
};

export default connect(
  null,
  mapDispatchToProps
)(Actions);
