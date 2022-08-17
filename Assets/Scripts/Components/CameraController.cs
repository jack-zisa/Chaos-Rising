using UnityEngine;

public class CameraController : MonoBehaviour
{
    public Transform target;
    public float zOffset;

    public float seekSpeed;
    private Vector3 seek;

    new private Camera camera;
    private void Start()
    {
        camera = Camera.main;
    }

    private void Update()
    {
        if (Input.mouseScrollDelta.y != 0)
        {
            if (Input.mouseScrollDelta.y > 0 && camera.orthographicSize < .2f)
            {
                print("out");
                // ZOOM OUT
                camera.orthographicSize += .1f;
            }
            if (Input.mouseScrollDelta.y < 0 && camera.orthographicSize > 1.8f)
            {
                print("in");
                // ZOOM IN
                camera.orthographicSize -= .1f;
            }
        }
    }

    private void FixedUpdate()
    {
        seek = Input.mousePosition;
        seek.x -= Screen.width / 2f;
        seek.y -= Screen.height / 2f;
        seek.x /= Screen.width;
        seek.y /= Screen.width;
        seek.z = zOffset;
        transform.position = Vector3.Lerp(transform.position, target.position + seek, seekSpeed);
    }
}
