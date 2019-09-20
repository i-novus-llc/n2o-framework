import React from 'react';
import PropTypes from 'prop-types';
import { UncontrolledPopover, PopoverBody } from 'reactstrap';
import { id } from '../../../utils/id';

class N2OPopover extends React.Component {
  constructor(props) {
    super(props);

    this.fieldId = id();
  }

  render() {
    const { help, placement, icon } = this.props;
    return (
      <div className={'n2o-popover'}>
        <button className={'bg-transparent border-0 n2o-popover-btn'} id={this.fieldId}>
          <i className={icon} />
        </button>
        <UncontrolledPopover
          className={'n2o-popover-body'}
          placement={placement}
          target={this.fieldId}
          trigger={'focus'}
        >
          <PopoverBody>
            <div dangerouslySetInnerHTML={{ __html: help }} />
          </PopoverBody>
        </UncontrolledPopover>
      </div>
    );
  }
}

N2OPopover.propTypes = {
  help: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
};

N2OPopover.defaultProps = {
  placement: 'right',
  icon: 'fa fa-question-circle',
};

export default N2OPopover;
