import React from 'react'
import { UncontrolledPopover, PopoverBody } from 'reactstrap'
import type { Placement } from 'popper.js'
import Dompurify from 'dompurify'

import { id } from '../utils/id'

type HelpPopoverProps = {
    help: string,
    icon?: string,
    placement?: Placement,
}
export const HelpPopover = ({
    help,
    placement = 'right',
    icon = 'fa fa-question-circle',
}: HelpPopoverProps) => {
    const fieldId = id()

    return (

        <div className="n2o-popover">
            <button
                className="n2o-popover-btn"
                id={fieldId}
                type="button"
            >
                <i className={icon} />
            </button>
            <UncontrolledPopover
                className="n2o-popover-body"
                placement={placement}
                target={fieldId}
                trigger="focus"
            >
                <PopoverBody>
                    {/* eslint-disable-next-line react/no-danger */}
                    <div dangerouslySetInnerHTML={{ __html: Dompurify.sanitize(help) }} />
                </PopoverBody>
            </UncontrolledPopover>
        </div>
    )
}
