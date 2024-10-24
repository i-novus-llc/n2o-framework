import React, { CSSProperties } from 'react'

import { TextSegment, TimeStampSegment, CloseButtonSegment, StacktraceSegment } from './utils'

/**
 * AlertSection - можно складывать в единый блок
 * TextSegment, TimeStampSegment, CloseButtonSegment, StacktraceSegment - опциональны
 * @example
 * двухуровневый Alert
 * (on top -> title + close + timestamp) , (on bottom -> text)
 * <div>
 *   <AlertSection text={title}
 timestamp={timestamp}
 closeButton={closeButton}
 onClick={batchedActionToClose}
 />
 <AlertSection text={text} />
 </div>
 */

interface Props {
    text?: string
    timestamp?: string
    closeButton?: boolean
    stacktraceVisible: boolean
    textClassName?: string
    isSimple?: boolean
    onClick(): void
    style?: CSSProperties
    stacktrace: string
    t(key: string): string
}

export const AlertSection = ({
    text,
    timestamp,
    closeButton,
    onClick,
    stacktrace,
    stacktraceVisible,
    textClassName,
    style,
    isSimple,
    t,
}: Props) => (
    <div className="n2o-alert-segment w-100 d-flex flex-row flex-nowrap" style={style}>
        <TextSegment text={text} className={textClassName} />
        <TimeStampSegment timestamp={timestamp} text={text} />
        <CloseButtonSegment
            closeButton={closeButton}
            onClick={onClick}
            text={text}
            timestamp={timestamp}
            extended={isSimple}
        />
        <StacktraceSegment
            stacktrace={stacktrace}
            onClick={onClick}
            stacktraceVisible={stacktraceVisible}
            t={t}
        />
    </div>
)
