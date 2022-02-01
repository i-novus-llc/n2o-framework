import React, { createRef } from 'react'
import PropTypes from 'prop-types'
import { UncontrolledPopover, PopoverBody } from 'reactstrap'

import { id } from '../../../../../utils/id'

class HelpPopover extends React.Component {
    constructor(props) {
        super(props)

        this.fieldId = id()
        this.button = createRef()
    }

  focusOnClick = () => {
      this.button.current.focus()
  };

  render() {
      const { help, placement, icon } = this.props

      return (
          <div className="n2o-popover">
              <button
                  onClick={this.focusOnClick}
                  className="n2o-popover-btn"
                  id={this.fieldId}
                  ref={this.button}
                  type="button"
              >
                  <i className={icon} />
              </button>
              <UncontrolledPopover
                  className="n2o-popover-body"
                  placement={placement}
                  target={this.fieldId}
                  trigger="focus"
              >
                  <PopoverBody>
                      {/* eslint-disable-next-line react/no-danger */}
                      <div dangerouslySetInnerHTML={{ __html: help }} />
                  </PopoverBody>
              </UncontrolledPopover>
          </div>
      )
  }
}

HelpPopover.propTypes = {
    help: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
    placement: PropTypes.string,
    icon: PropTypes.string,
}

HelpPopover.defaultProps = {
    placement: 'right',
    icon: 'fa fa-question-circle',
}

export default HelpPopover
