import React from 'react';
import { compose } from 'recompose';
import PropTypes from 'prop-types';
import { isEqual, get } from 'lodash';
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
  }

  componentDidUpdate(prevProps) {
    if (!isEqual(prevProps.value, this.props.value)) {
      this.setState({ value: this.getValueFromModel(this.props) });
    }
  }

  getValueFromModel(props) {
    const { model, fieldKey, id } = props;
    return get(model, fieldKey || id);
  }

  onChange(value) {
    this.setState({ value });
  }

  toggleEdit() {
    const {
      model,
      id,
      prevResolveModel,
      onResolve,
      widgetId,
      callInvoke,
    } = this.props;
    let newState = {
      editing: !this.state.editing,
    };
    if (!isEqual(prevResolveModel, model)) {
      onResolve(widgetId, model);
    }
    if (!newState.editing && !isEqual(this.state.prevValue, this.state.value)) {
      callInvoke({
        ...model,
        [id]: this.state.value,
      });
    }

    newState = {
      ...newState,
      prevValue: this.state.value,
    };

    this.setState(newState);
  }

  render() {
    const { visible, control, editable, ...rest } = this.props;
    const { value, editing } = this.state;
    return (
      visible && (
        <div
          className="n2o-editable-cell"
          onClick={e => e.stopPropagation()}
          ref={el => (this.node = el)}
        >
          {!editing && (
            <div className="n2o-editable-cell-text" onClick={this.toggleEdit}>
              <Text text={value} {...rest} />
            </div>
          )}
          {editable && editing && (
            <div className="n2o-editable-cell-control">
              {React.createElement(control.component, {
                ...control,
                className: 'n2o-advanced-table-edit-control',
                onChange: this.onChange,
                onBlur: this.toggleEdit,
                autoFocus: true,
                value: value,
              })}
            </div>
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
  disabled: false,
};

EditableCell.defaultProps = {
  visible: true,
  disabled: false,
};

export default compose(
  withActionsEditableCell,
  withCell
)(EditableCell);
