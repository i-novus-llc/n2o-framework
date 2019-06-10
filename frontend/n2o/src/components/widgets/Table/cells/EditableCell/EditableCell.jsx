import React from 'react';
import cn from 'classnames';
import { compose } from 'recompose';
import { HotKeys } from 'react-hotkeys';
import PropTypes from 'prop-types';
import { isEqual, get, isObject } from 'lodash';
import Text from '../../../../snippets/Text/Text';
import withActionsEditableCell from './withActionsEditableCell';
import withCell from '../../withCell';

/**
 * Компонент редактируемой ячейки таблицы
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {object} control - настройки компонента фильтрации
 * @reactProps {boolean} editable - флаг разрешения редактирования
 * @reactProps {string} value - значение ячейки
 * @reactProps {boolean} disabled - флаг активности
 */
export class EditableCell extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value: this.getValueFromModel(props),
      editing: false,
      prevValue: this.getValueFromModel(props),
    };

    this.onChange = this.onChange.bind(this);
    this.toggleEdit = this.toggleEdit.bind(this);
    this.getValueFromModel = this.getValueFromModel.bind(this);
    this.handleKeyDown = this.handleKeyDown.bind(this);
    this.callAction = this.callAction.bind(this);
  }

  componentDidUpdate(prevProps, prevState) {
    if (!isEqual(prevProps.value, this.props.value)) {
      this.setState({ value: this.getValueFromModel(this.props) });
    }

    if (
      !this.state.editing &&
      isEqual(prevState.prevValue, prevState.value) &&
      !isEqual(this.state.prevValue, this.state.value)
    ) {
      {
        this.callAction(this.state.value);
      }
    }
  }

  getValueFromModel(props) {
    const { model, fieldKey, id } = props;
    return get(model, fieldKey || id);
  }

  onChange(value) {
    this.setState(() => ({ value }));
  }

  toggleEdit() {
    const {
      model,
      prevResolveModel,
      onResolve,
      onSetSelectedId,
      widgetId,
    } = this.props;
    let newState = {
      editing: !this.state.editing,
    };
    if (!isEqual(prevResolveModel, model)) {
      onResolve(widgetId, model);
      onSetSelectedId();
    }
    if (!newState.editing && !isEqual(this.state.prevValue, this.state.value)) {
      this.callAction(this.state.value);
    }

    newState = {
      ...newState,
      prevValue: this.state.value,
    };

    this.setState(newState);
  }

  callAction(value) {
    const { model, id, callActionImpl, action } = this.props;
    callActionImpl(
      {},
      {
        action,
        model: {
          ...model,
          [id]: value,
        },
      },
      true
    );
  }

  handleKeyDown() {
    this.toggleEdit();
  }

  render() {
    const {
      visible,
      control,
      editable,
      parentWidth,
      parentHeight,
      valueFieldId,
      format,
      ...rest
    } = this.props;

    const { value, editing } = this.state;
    const style = {
      width: parentWidth,
      height: parentHeight,
    };

    const events = { events: 'enter' };
    const handlers = { events: this.handleKeyDown };

    return (
      visible && (
        <div
          style={style}
          className={cn({ 'n2o-editable-cell': editable })}
          onClick={e => e.stopPropagation()}
        >
          {!editing && (
            <div
              className="n2o-editable-cell-text"
              onClick={editable && this.toggleEdit}
            >
              <Text
                text={isObject(value) ? value[valueFieldId] : value}
                format={format}
              />
            </div>
          )}
          {editable && editing && (
            <HotKeys keyMap={events} handlers={handlers}>
              <div className="n2o-editable-cell-control" style={style}>
                {React.createElement(control.component, {
                  ...control,
                  className: 'n2o-advanced-table-edit-control',
                  onChange: this.onChange,
                  onBlur: this.toggleEdit,
                  autoFocus: true,
                  value: value,
                  openOnFocus: true,
                  showButtons: false,
                  resetOnNotValid: false,
                })}
              </div>
            </HotKeys>
          )}
        </div>
      )
    );
  }
}

EditableCell.propTypes = {
  visible: PropTypes.bool,
  control: PropTypes.object,
  editable: PropTypes.bool,
  value: PropTypes.string,
  disabled: PropTypes.bool,
  valueFieldId: PropTypes.string,
};

EditableCell.defaultProps = {
  visible: true,
  disabled: false,
  valueFieldId: 'id',
};

export default compose(
  withActionsEditableCell,
  withCell
)(EditableCell);
