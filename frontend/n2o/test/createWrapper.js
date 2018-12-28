/**
 * Created by emamoshin on 26.09.2017.
 */
export const createWrapper = (Component, propOverrides) => {
  const props = Object.assign({
    // use this to assign some default props
  }, propOverrides);

  let wrapper;

  if (method === "mount") {
    wrapper = mount(<Pagination {...props} />);
  } else {
    wrapper = shallow(<Pagination {...props} />);
  }

  return {
    props,
    wrapper,
  };
};