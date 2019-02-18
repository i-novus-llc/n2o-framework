import React from 'react';
import PropTypes from 'prop-types';
import { isEqual, get } from 'lodash';
import withEditableActions from './withEditableActions';
import Text from '../../../../snippets/Text/Text';
import Factory from '../../../../../core/factory/Factory';

class EditableCell extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value: this.getValueFromModel(props),
      editing: false
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
    this.setState({ editing: !this.state.editing });
  }

  render() {
    const { visible, control, editable, ...rest } = this.props;
    const { value, editing } = this.state;
    return (
      <div className="n2o-editable-cell" onClick={e => e.stopPropagation()}>
        {visible &&
          !editing && (
            <div className="n2o-editable-cell-text" onClick={this.toggleEdit}>
              <Text text={value} {...rest} />
            </div>
          )}
        {editable &&
          editing && (
            <div className="n2o-editable-cell-control">
              {React.createElement(control.component, {
                ...control,
                className: 'n2o-advanced-table-edit-control',
                onChange: this.onChange,
                onBlur: this.toggleEdit,
                autoFocus: true,
                value: value
              })}
            </div>
          )}
      </div>
    );
  }
}

EditableCell.defaultProps = {
  visible: true,
  disabled: false
};

export default withEditableActions(EditableCell);
