import classNames from 'classnames'
import React, { FC, HTMLAttributes } from 'react'

type ViewProps = Pick<HTMLAttributes<HTMLDivElement>, 'className'> & { maxRows?: number }

export const View: FC<ViewProps> = ({ children, className, maxRows }) => (
    <div className={classNames('n2o-editable-cell-text', className)}>
        <span className="n2o-snippet text-truncate" data-max-rows={maxRows}>{children}</span>
    </div>
)
