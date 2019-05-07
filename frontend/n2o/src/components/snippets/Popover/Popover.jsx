import React from 'react';
import PropTypes from 'prop-types';
import { Popover, PopoverBody } from 'reactstrap';
import { id } from '../../../utils/id';

class N2OPopover extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      showPopover: false,
    };
    this.fieldId = id();
    this.onToggle = this.onToggle.bind(this);
  }

  onToggle() {
    this.setState({
      showPopover: !this.state.showPopover,
    });
  }

  render() {
    const { help, placement, icon } = this.props;
    return (
      <div className={'n2o-popover'}>
        <div id={this.fieldId} onClick={this.onToggle}>
          <i className={icon} />
        </div>
        <Popover
          placement={placement}
          isOpen={this.state.showPopover}
          target={this.fieldId}
          toggle={this.onToggle}
        >
          <PopoverBody>
            <div dangerouslySetInnerHTML={{ __html: help }} />
          </PopoverBody>
        </Popover>
      </div>
    );
  }
}

N2OPopover.propTypes = {
  help: PropTypes.oneOf(PropTypes.string, PropTypes.node),
};

N2OPopover.defaultProps = {
  placement: 'right',
  icon: 'fa fa-question-circle',
};

export default N2OPopover;
