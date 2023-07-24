import React from 'react'

import { TextSegment, TimeStampSegment, CloseButtonSegment, StacktraceSegment } from './utils'
import { AlertSectionProps } from './types'

/**
 * AlertSection - можно складывать в едицый блок
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
}: AlertSectionProps) => (
    <div className="n2o-alert-segment w-100 d-flex flex-row flex-nowrap" style={style || {}}>
        <TextSegment text={text || ''} className={textClassName} />
        <TimeStampSegment timestamp={timestamp} text={text || ''} />
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
