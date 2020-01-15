import React from 'react';
import cn from 'classnames';
import { compose } from 'recompose';
import { HotKeys } from 'react-hotkeys/cjs';
import PropTypes from 'prop-types';
import isEqual from 'lodash/isEqual';
import get from 'lodash/get';
import set from 'lodash/set';
import Text from '../../../../snippets/Text/Text';
import withActionsEditableCell from './withActionsEditableCell';
import withCell from '../../withCell';
import onClickOutside from 'react-onclickoutside';
import moment from 'moment';

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
    this.onFocus = this.onFocus.bind(this);
    this.onBlur = this.onBlur.bind(this);
    this.handleKeyDown = this.handleKeyDown.bind(this);
    this.callAction = this.callAction.bind(this);
  }

  componentDidUpdate(prevProps, prevState) {
    if (!isEqual(prevProps.model, this.props.model)) {
      this.setState({
        prevModel: this.state.model,
        model: this.props.prevResolveModel,
      });
    } else if (
      !isEqual(prevProps.prevResolveModel, this.props.prevResolveModel) &&
      this.props.prevResolveModel.id === this.state.model.id
    ) {
      this.setState({
        prevModel: this.state.model,
        model: this.props.prevResolveModel,
      });
    }

    if (
      !this.state.editing &&
      isEqual(prevState.prevModel, prevState.model) &&
      !isEqual(this.state.prevModel, this.state.model) &&
      !isEqual(this.state.model, this.props.prevResolveModel)
    ) {
      this.callAction(this.state.model);
    }
  }

  handleClickOutside(e) {
    this.setState({
      editing: false,
    });
  }

  onBlur() {
    const { model, prevResolveModel, onSetSelectedId } = this.props;
    let newState = {
      editing: !this.state.editing,
    };
    if (!isEqual(get(prevResolveModel, 'id'), get(model, 'id'))) {
      onSetSelectedId();
    }
    if (!newState.editing && !isEqual(this.state.prevModel, this.state.model)) {
      this.callAction(this.state.model);
    }

    newState = {
      prevModel: this.state.model,
      editing: false,
    };

    this.setState(newState);
  }

  onChange(value) {
    const { model, prevResolveModel, onSetSelectedId } = this.props;
    const newModel = Object.assign({}, this.state.model);
    const { editFieldId } = this.props;
    set(newModel, editFieldId, value);
    if (!isEqual(newModel, this.state.model)) {
      this.callAction(newModel);
    }
    if (!isEqual(get(prevResolveModel, 'id'), get(model, 'id'))) {
      onSetSelectedId();
    }
    this.setState({
      model: newModel,
      editing: false,
      prevModel: this.state.model,
    });
  }

  onFocus() {
    this.setState({
      editing: true,
    });
  }

  callAction(model) {
    const { callInvoke, action, onResolve, widgetId } = this.props;
    const dataProvider = get(action, 'options.payload.dataProvider');
    const meta = get(action, 'options.meta');

    callInvoke(model, dataProvider, meta);
    onResolve(widgetId, model);
  }

  handleKeyDown() {
    this.onBlur();
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
              onClick={editable && this.onFocus}
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
  withCell,
  onClickOutside
)(EditableCell);
