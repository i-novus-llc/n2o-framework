import React from 'react';
import TooltipTrigger from 'react-popper-tooltip';
import isArray from 'lodash/isArray';
import isUndefined from 'lodash/isUndefined';
import map from 'lodash/map';
import get from 'lodash/get';
import cn from 'classnames';
import 'react-popper-tooltip/dist/styles.css';
import TooltipHOC from '../../../../snippets/Tooltip/Tooltip';

function Tooltip(props) {
  const { model, fieldKey, label, id, trigger, labelDashed } = props;

  const tooltipList = model && get(model, fieldKey);
  const validTooltipList = model && fieldKey && isArray(tooltipList);
  const listLength = validTooltipList ? tooltipList.length : 0;
  const triggerClassName = cn({
    'list-text-cell__trigger_dashed': labelDashed,
    'list-text-cell__trigger': !labelDashed || isUndefined(labelDashed),
  });
  const withPlaceholder = label => label.replace(/{value}/gm, listLength);

  //ищет placeholder {value} в label, заменяет его на длину массива
  const replacePlaceholder = label => {
    const placeholder = '{value}';
    const hasPlaceholder = !isUndefined(label) && label.match(placeholder);
    if (isUndefined(label)) {
      return listLength;
    } else if (hasPlaceholder) {
      return withPlaceholder(label);
    } else {
      return `${listLength + ' ' + label}`;
    }
  };

  //trigger для появления tooltip, отображает label
  const Trigger = () => {
    return (
      <span className={triggerClassName} id={id}>
        {replacePlaceholder(label)}
      </span>
    );
  };

  //hint отображает лист полученный из model
  const tooltipBody = () => {
    return (
      <div>
        {validTooltipList &&
          map(tooltipList, (tooltipItem, index) => (
            <div key={index}>{tooltipItem}</div>
          ))}
      </div>
    );
  };

  return (
    <>
      <Trigger />
      <TooltipHOC
        target={id}
        trigger={trigger}
        hint={tooltipBody()}
        {...props}
      />
    </>
  );
}

export default Tooltip;

// function Tooltip(props) {
//   const { model, fieldKey, label } = props;
//   const tooltipList = model && get(model, fieldKey);
//   const validTooltipList = model && fieldKey && isArray(tooltipList);
//   const listLength = validTooltipList ? tooltipList.length : 0;
//
//   //ищет placeholder {value} в label, заменяет на длину массива
//   const withPlaceholder = label => {
//     const placeholder = '{value}';
//     const hasPlaceholder = !isUndefined(label) && label.match(placeholder);
//     if (isUndefined(label)) {
//       return listLength;
//     } else if (hasPlaceholder) {
//       return label.replace(/{value}/gm, listLength);
//     } else {
//       return `${listLength + ' ' + label}`;
//     }
//   };
//
//   const Trigger = ({ getTriggerProps, triggerRef }) => {
//     return (
//       <span
//         {...getTriggerProps({
//           ref: triggerRef,
//           className: cn({
//             'list-text-cell__trigger_dashed': props.labelDashed,
//             'list-text-cell__trigger': !props.labelDashed,
//           }),
//         })}
//       >
//         {withPlaceholder(label)}
//       </span>
//     );
//   };
//
//   const TooltipBody = ({
//     getTooltipProps,
//     getArrowProps,
//     tooltipRef,
//     arrowRef,
//     placement,
//   }) => {
//     return (
//       <div
//         {...getTooltipProps({
//           ref: tooltipRef,
//           className: cn({
//             'list-text-cell__tooltip-container': validTooltipList,
//             'list-text-cell__tooltip-container_empty': !validTooltipList,
//           }),
//         })}
//       >
//         {validTooltipList && (
//           <div
//             {...getArrowProps({
//               ref: arrowRef,
//               'data-placement': placement,
//               className: 'tooltip-arrow',
//             })}
//           />
//         )}
//         {validTooltipList &&
//           map(tooltipList, (tooltipItem, index) => (
//             <div
//               key={index}
//               className="list-text-cell__tooltip-container__body"
//             >
//               {tooltipItem}
//             </div>
//           ))}
//       </div>
//     );
//   };
//   return (
//     <TooltipTrigger
//       placement={props.placement}
//       trigger={props.trigger}
//       tooltip={TooltipBody}
//       fieldKey={props.fieldKey}
//       label={props.label}
//       labelDashed={props.labelDashed}
//     >
//       {Trigger}
//     </TooltipTrigger>
//   );
// }
//
// export default Tooltip;
