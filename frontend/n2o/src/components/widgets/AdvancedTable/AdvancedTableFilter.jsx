import React, { Component } from 'react';
import { isEmpty } from 'lodash';
import DropDown from 'rc-dropdown';
import { Button } from 'reactstrap';
import 'rc-dropdown/assets/index.css';
import 'rc-menu/assets/index.css';
import InputText from '../../controls/InputText/InputText';

function AdvancedTableFilterOverlay({ value, onChange, onResetFilter, onSetFilter }) {
  return (
    <div className="n2o-advanced-table-filter-dropdown">
      <InputText value={value} onChange={onChange} />
      <Button color={'primary'} size={'sm'} onClick={onSetFilter}>
        Искать
      </Button>
      <Button size={'sm'} onClick={onResetFilter}>
        Сбросить
      </Button>
    </div>
  );
}

class AdvancedTableFilter extends Component {
  constructor(props) {
    super(props);

    this.state = {
      value: ''
    };

    this.onChangeFilter = this.onChangeFilter.bind(this);
    this.onResetFilter = this.onResetFilter.bind(this);
    this.onSetFilter = this.onSetFilter.bind(this);
  }

  onChangeFilter(value) {
    this.setState({ value });
  }

  onResetFilter() {
    if (!isEmpty(this.state.value)) {
      const { id, onFilter } = this.props;
      this.setState({ value: '' }, () => onFilter({ id, value: '' }));
    }
  }

  onSetFilter() {
    const { onFilter, id } = this.props;
    onFilter &&
      onFilter({
        id,
        value: this.state.value
      });
  }

  render() {
    return (
      <DropDown
        trigger={['click']}
        onVisibleChange={this.props.onVisibleChange}
        visible={this.props.visible}
        onBlur={() => this.onVisibleChange(false)}
        overlay={() => (
          <AdvancedTableFilterOverlay
            value={this.state.value}
            onChange={this.onChangeFilter}
            onResetFilter={this.onResetFilter}
            onSetFilter={this.onSetFilter}
          />
        )}
      >
        <React.Fragment>
          {this.props.children}
          <Button size={'sm'} onClick={this.props.onVisibleChange}>
            <i className="fa fa-filter" />
          </Button>
        </React.Fragment>
      </DropDown>
    );
  }
}

AdvancedTableFilter.propTypes = {};

export default AdvancedTableFilter;
