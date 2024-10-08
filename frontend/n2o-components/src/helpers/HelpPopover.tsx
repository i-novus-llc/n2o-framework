import React, { memo } from 'react'
import { UncontrolledPopover, PopoverBody, PopoverProps } from 'reactstrap'
import Dompurify from 'dompurify'

import { id } from '../utils/id'

interface Props {
    help: string
    icon?: string
    placement?: PopoverProps['placement']
}
export const HelpPopover = memo(
    ({ help, placement = 'right', icon = 'fa fa-question-circle' }: Props) => {
        const target = id()

        return (
            <div className="n2o-popover">
                <button aria-label="popover-btn" className="n2o-popover-btn" id={target} type="button">
                    <i className={icon} />
                </button>
                <UncontrolledPopover
                    className="n2o-popover-body"
                    placement={placement}
                    target={target}
                    trigger="focus"
                >
                    <PopoverBody>
                        {/* eslint-disable-next-line react/no-danger */}
                        <div dangerouslySetInnerHTML={{ __html: Dompurify.sanitize(help) }} />
                    </PopoverBody>
                </UncontrolledPopover>
            </div>
        )
    },

)
