import { compose, mapProps } from 'recompose';
import { push } from 'connected-react-router';
import withActionButton from '../withActionButton';
import compileUrl from '../../../utils/compileUrl';
import mappingProps from '../Simple/mappingProps';

function isModifiedEvent(event) {
  return !!(event.metaKey || event.altKey || event.ctrlKey || event.shiftKey);
}

export default compose(
  withActionButton({
    onClick: (e, props, state) => {
      e.preventDefault();
      const { url, pathMapping, queryMapping, target } = props;
      const compiledUrl = compileUrl(url, { pathMapping, queryMapping }, state);

      if (isModifiedEvent(e)) {
        return;
      }
      if (props.inner) {
        props.dispatch(push(compiledUrl));
      } else if (target === '_blank') {
        window.open(compiledUrl);
      } else {
        window.location = compiledUrl;
      }
    },
  }),
  mapProps(props => ({
    ...mappingProps(props),
    url: props.url,
    href: props.url,
    target: props.target === 'newWindow' ? '_blank' : props.target,
    tag: 'a',
  }))
);
