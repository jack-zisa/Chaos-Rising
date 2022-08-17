using UnityEngine;
using UnityEngine.EventSystems;

public class ItemPickAndPlace : MonoBehaviour, IPointerDownHandler, IBeginDragHandler, IEndDragHandler, IDragHandler
{
    private RectTransform rectTransform;

    private void Start()
    {
        rectTransform = GetComponent<RectTransform>();
    }

    public void OnPointerDown(PointerEventData eventData)
    {
        Debug.Log("down");
    }

    public void OnBeginDrag(PointerEventData eventData)
    {
        print("drag");
    }

    public void OnEndDrag(PointerEventData eventData)
    {
        print("end drag");
    }

    public void OnDrag(PointerEventData eventData)
    {
        print("drag");
        rectTransform.anchoredPosition = eventData.position;
    }

    public void OnDrop(PointerEventData eventData)
    {
        print("drop");
    }
}
