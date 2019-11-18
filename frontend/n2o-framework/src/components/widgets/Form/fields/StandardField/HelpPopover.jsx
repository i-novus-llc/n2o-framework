import React from 'react';
import PropTypes from 'prop-types';
import UncontrolledPopover from 'reactstrap/lib/UncontrolledPopover';
import PopoverBody from 'reactstrap/lib/PopoverBody';
import { id } from '../../../../../utils/id';

class HelpPopover extends React.Component {
  constructor(props) {
    super(props);

    this.fieldId = id();
  }

  render() {
    const { help, placement, icon } = this.props;
    return (
      <div className={'n2o-popover'}>
        <Button className={'n2o-popover-btn'} id={this.fieldId} color="link">
          <i className={icon} />
        </Button>
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

HelpPopover.propTypes = {
  help: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
};

HelpPopover.defaultProps = {
  placement: 'right',
  icon: 'fa fa-question-circle',
};

export default HelpPopover;
