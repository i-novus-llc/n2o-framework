import React from 'react';
import cn from 'classnames';
import { compose } from 'recompose';
import { HotKeys } from 'react-hotkeys';
import PropTypes from 'prop-types';
import { isEqual, get, isObject, set } from 'lodash';
import Text from '../../../../snippets/Text/Text';
import withActionsEditableCell from './withActionsEditableCell';
import withCell from '../../withCell';

/**
 * Компонент редактируемой ячейки таблицы
 * @reactProps {boolean} visible - флаг видимости
 * @reactProps {object} control - настройки компонента фильтрации
 * @reactProps {boolean} editable - флаг разрешения редактирования
 * @reactProps {string} model - значение модели
 * @reactProps {boolean} disabled - флаг активности
 */
export class EditableCell extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      model: props.model || {},
      editing: false,
      prevModel: {},
    };

    this.onChange = this.onChange.bind(this);
    this.toggleEdit = this.toggleEdit.bind(this);
    this.handleKeyDown = this.handleKeyDown.bind(this);
    this.callAction = this.callAction.bind(this);
  }

  componentDidUpdate(prevProps, prevState) {
    if (!isEqual(prevProps.model, this.props.model)) {
      this.setState({ model: this.props.model });
    }

    if (
      !this.state.editing &&
      isEqual(prevState.prevModel, prevState.model) &&
      !isEqual(this.state.prevModel, this.state.model)
    ) {
      this.callAction(this.state.model);
    }
  }

  onChange(value) {
    const newModel = Object.assign({}, this.state.model);
    const { editFieldId } = this.props;
    set(newModel, editFieldId, value);
    this.setState({
      model: newModel,
    });
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
    if (!newState.editing && !isEqual(this.state.prevModel, this.state.model)) {
      this.callAction(this.state.model);
    }

    newState = {
      ...newState,
      prevModel: this.state.model,
    };

    this.setState(newState);
  }

  callAction(model) {
    const { callInvoke, action } = this.props;
    const dataProvider = get(action, 'options.payload.dataProvider');
    const meta = get(action, 'options.meta');

    callInvoke(model, dataProvider, meta);
  }

  handleKeyDown() {
    this.toggleEdit();
  }

  stopPropagation(e) {
    e.stopPropagation();
  }

  render() {
    const {
      visible,
      control,
      editable,
      format,
      fieldKey,
      editFieldId,
    } = this.props;
    const { editing, model } = this.state;
    const events = { events: 'enter' };
    const handlers = { events: this.handleKeyDown };

    return (
      visible && (
        <div
          className={cn({ 'n2o-editable-cell': editable })}
          onClick={this.stopPropagation}
        >
          {!editing && (
            <div
              className="n2o-editable-cell-text"
              onClick={editable && this.toggleEdit}
            >
              <Text text={get(model, fieldKey)} format={format} />
            </div>
          )}
          {editable && editing && (
            <HotKeys keyMap={events} handlers={handlers}>
              <div className="n2o-editable-cell-control">
                {React.createElement(control.component, {
                  ...control,
                  className: 'n2o-advanced-table-edit-control',
                  onChange: this.onChange,
                  onBlur: this.toggleEdit,
                  autoFocus: true,
                  value: get(model, editFieldId),
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
  editFieldId: PropTypes.string,
};

EditableCell.defaultProps = {
  visible: true,
  disabled: false,
};

export default compose(
  withActionsEditableCell,
  withCell
)(EditableCell);
