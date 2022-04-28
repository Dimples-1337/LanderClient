#version 120

varying vec4 color;
varying vec4 texcoord;
varying vec4 lmcoord;


attribute vec4 mc_Entity;

uniform int worldTime;
uniform float rainStrength;

varying vec3 normal;
varying vec3 tangent;
varying vec3 binormal;
varying vec3 viewVector;
varying vec2 waves;

varying float distance;
varying float translucent;

#define WAVING_GRASS
#define WAVING_WHEAT
#define WAVING_LEAVES
//#define WAVING_FIRE

void main() {

	texcoord = gl_MultiTexCoord0;
	
	vec4 position = gl_Vertex;
	
	translucent = 0.0f;

	/*
	//Natural Textures
		if (
		
			mc_Entity.x == 12.0 ||       //sand
			mc_Entity.x == 2.0  ||       //grass
			mc_Entity.x == 3.0  ||       //dirt
			mc_Entity.x == 18.0 ||       //leaves
			mc_Entity.x == 13.0       //gravel
			
			) 	{ 
			
			
			
				float speed = 0.3;
		
				float magnitude = sin((position.x * 4.0 / (28.0)) + position.x + position.z) * 0.1 + 0.1;
				float d0 = sin(position.z * 1.0 / (422.0 * speed)) * 3.0 - 1.5 + position.z;
				float d1 = sin(position.x * 6.0 / (852.0 * speed)) * 3.0 - 1.5 + position.x;
				float d3 = sin(position.x * 10.0 / (1052.0 * speed)) * 3.0 - 1.5 + position.z;
		
				waves.x += sin((position.z * 23.0 / (58.0 * speed)) + (position.x + d0) * 0.1 + (position.z + d1) * 0.1) * magnitude;
				waves.y += sin((position.x * 1.0 / (28.0 * speed)) + (position.z + d1) * 0.1 + (position.x + d3) * 0.1) * magnitude;
		
				}
	
		//small movement
		if (
		
			mc_Entity.x == 12.0 ||       //sand
			mc_Entity.x == 2.0  ||       //grass
			mc_Entity.x == 3.0  ||       //dirt
			mc_Entity.x == 18.0 ||       //leaves
			mc_Entity.x == 13.0       //gravel
			
			) {
			
			
		float speed = 0.05;
		
		float magnitude = (sin(((position.y + position.x)/2.0 + 1.0f * 3.0 / ((28.0)))) * 0.05 + 0.15) * 0.4;
		float d0 = sin(position.y * 2.0 / (912.0 * speed)) * 3.0 - 1.5;
        float d2 = sin(position.z * 6.0 / (112.0 * speed)) * 3.0 - 1.5;
		float d3 = sin(position.x * 1.0 / (342.0 * speed)) * 3.0 - 1.5;

		
		waves.x += sin((position.x * 24.0 / (31.0 * speed)) + (-position.x + d0)*1.6 + (position.z + d3)*1.6) * magnitude;
		waves.y += sin((position.z * 64.0 / (18.0 * speed)) + (position.z + d2)*1.6 + (-position.x + d3)*1.6) * magnitude;
		
	}
	*/
#ifdef WAVING_GRASS	
//Grass//
	if (mc_Entity.x == 31.0 && texcoord.t < 0.15) {
		float speed = 8.0;
		
		float magnitude = sin((worldTime * 3.0 / (28.0)) + position.x + position.z) * 0.1 + 0.1;
		float d0 = sin(worldTime * 3.0 / (122.0 * speed)) * 3.0 - 1.5 + position.z;
		float d1 = sin(worldTime * 3.0 / (152.0 * speed)) * 3.0 - 1.5 + position.x;
		position.x += sin((worldTime * 3.0 / (28.0 * speed)) + (position.x + d0) * 0.1 + (position.z + d1) * 0.1) * magnitude * (1.0f + rainStrength * 1.4f);
		position.z += sin((worldTime * 3.0 / (28.0 * speed)) + (position.z + d0) * 0.1 + (position.x + d1) * 0.1) * magnitude * (1.0f + rainStrength * 1.4f);

		
		
	}
	
	if (mc_Entity.x == 31.0) {
		translucent = 1.0f;
	}
	
	
	//small leaf movement
	if (mc_Entity.x == 31.0 && texcoord.t < 0.15) {
		float speed = 0.8;
		
		float magnitude = (sin(((position.y + position.x)/2.0 + worldTime * 3.0 / ((28.0)))) * 0.05 + 0.15) * 0.4;
		float d0 = sin(worldTime * 3.0 / (112.0 * speed)) * 3.0 - 1.5;
		float d1 = sin(worldTime * 3.0 / (142.0 * speed)) * 3.0 - 1.5;
		position.z += sin((worldTime * 3.0 / (18.0 * speed)) + (position.z + d0)*1.6 + (-position.x + d1)*1.6) * magnitude * (1.0f + rainStrength * 1.7f);
		position.y += sin((worldTime * 3.0 / (11.0 * speed)) + (position.z + d0) + (position.x + d1)) * (magnitude/3.0) * (1.0f + rainStrength * 1.7f);
		translucent = 1.0f;
	}
	
#endif	


#ifdef WAVING_GRASS	
//Grass//
	if (mc_Entity.x == 111.0) {
		float speed = 8.0;
		
		float magnitude = sin((worldTime * 3.0 / (28.0)) + position.x + position.z) * 0.1 + 0.1;
		float d0 = sin(worldTime * 3.0 / (122.0 * speed)) * 3.0 - 1.5 + position.z;
		float d1 = sin(worldTime * 3.0 / (152.0 * speed)) * 3.0 - 1.5 + position.x;
		position.x += sin((worldTime * 3.0 / (28.0 * speed)) + (position.x + d0) * 0.1 + (position.z + d1) * 0.1) * magnitude;
		position.z += sin((worldTime * 3.0 / (28.0 * speed)) + (position.z + d0) * 0.1 + (position.x + d1) * 0.1) * magnitude;
		
		
	}
	
	if (mc_Entity.x == 111.0) {
		translucent = 1.0f;
	}
	
	
	//small leaf movement
	if (mc_Entity.x == 111.0) {
		float speed = 0.8;
		
		float magnitude = (sin(((position.y + position.x)/2.0 + worldTime * 3.0 / ((28.0)))) * 0.05 + 0.15) * 0.4;
		float d0 = sin(worldTime * 3.0 / (112.0 * speed)) * 3.0 - 1.5;
		float d1 = sin(worldTime * 3.0 / (142.0 * speed)) * 3.0 - 1.5;
        position.x += sin((worldTime * 3.0 / (18.0 * speed)) + (-position.x + d0)*1.6 + (position.z + d1)*1.6) * magnitude;
		position.y += sin((worldTime * 3.0 / (11.0 * speed)) + (position.z + d0) + (position.x + d1)) * (magnitude/3.0);
		translucent = 1.0f;
	}
	
#endif


#ifdef WAVING_WHEAT
//Wheat//
	if (mc_Entity.x == 59.0 && texcoord.t < 0.35) {
		float speed = 8.0;
		
		float magnitude = sin((worldTime * 3.0 / (28.0)) + position.x + position.z) * 0.02 + 0.02;
		float d0 = sin(worldTime * 3.0 / (122.0 * speed)) * 3.0 - 1.5 + position.z;
		float d1 = sin(worldTime * 3.0 / (152.0 * speed)) * 3.0 - 1.5 + position.x;
		position.x += sin((worldTime * 3.0 / (28.0 * speed)) + (position.x + d0) * 0.1 + (position.z + d1) * 0.1) * magnitude;
		position.z += sin((worldTime * 3.0 / (28.0 * speed)) + (position.z + d0) * 0.1 + (position.x + d1) * 0.1) * magnitude;
		translucent = 1.0f;
	}
	
	//small leaf movement
	if (mc_Entity.x == 59.0 && texcoord.t < 0.35) {
		float speed = 0.8;
		
		float magnitude = (sin(((position.y + position.x)/2.0 + worldTime * 3.0 / ((28.0)))) * 0.025 + 0.075) * 0.2;
		float d0 = sin(worldTime * 3.0 / (112.0 * speed)) * 3.0 - 1.5;
		float d1 = sin(worldTime * 3.0 / (142.0 * speed)) * 3.0 - 1.5;
		position.x += sin((worldTime * 3.0 / (18.0 * speed)) + (-position.x + d0)*1.6 + (position.z + d1)*1.6) * magnitude * (1.0f + rainStrength * 2.0f);
		translucent = 1.0f;
	}
	
	if (mc_Entity.x == 59.0) {
		translucent = 1.0f;
	}
#endif
	
	

#ifdef WAVING_LEAVES
//Leaves//



	
		
	if (mc_Entity.x == 18.0 && texcoord.t < 1.90 && texcoord.t > -1.0) {
		float speed = 1.0;
		
		float magnitude = (sin((position.y + position.x + worldTime * 3.0 / ((28.0) * speed))) * 0.15 + 0.15) * 0.20;
		float d1 = sin(worldTime * 3.0 / (142.0 * speed)) * 3.0 - 1.5;
		float d3 = sin(worldTime * 3.0 / (122.0 * speed)) * 3.0 - 1.5;
		position.x += sin((worldTime * 3.0 / (18.0 * speed)) + (-position.x + d3)*1.6 + (position.z + d1)*1.6) * magnitude * (1.0f + rainStrength * 1.0f);
		
	}
	
/*
	//lower leaf movement
	if (mc_Entity.x == 18.0 && texcoord.t > 0.20) {
		float speed = 0.75;
		
		float magnitude = (sin((worldTime * 3.0 / ((28.0) * speed))) * 0.05 + 0.15) * 0.1;
		float d1 = sin(worldTime * 3.0 / (142.0 * speed)) * 3.0 - 1.5;
		float d3 = sin(worldTime * 3.0 / (112.0 * speed)) * 3.0 - 1.5;
		position.z += sin((worldTime * 3.0 / (16.0 * speed)) + (position.z + d1)*0.9 + (position.x + d3)*0.9) * magnitude;
		position.y += sin((worldTime * 3.0 / (15.0 * speed)) + (position.z + d1) + (position.x + d3)) * (magnitude/1.0);
	}
*/
#endif	


#ifdef WAVING_FIRE
//Fire//
	if (mc_Entity.x == 51.0 && texcoord.t < 0.10) {
		float magnitude = 0.3;
		float d2 = sin(worldTime * 3.0 / 92.0) * 1.0 + 1.5 + position.z;
		float d3 = sin(worldTime * 3.0 / 92.0) * 1.0 + 1.5 + position.x;
		position.z += sin((worldTime * 3.0 / 18.0) + (position.z + d2) * 2.0 + (position.x + d3) * 1.0) * magnitude;
		translucent = 1.0f;
	}
#endif

	vec4 locposition = gl_ModelViewMatrix * gl_Vertex;
	
	distance = sqrt(locposition.x * locposition.x + locposition.y * locposition.y + locposition.z * locposition.z);


	gl_Position = gl_ProjectionMatrix * (gl_ModelViewMatrix * position);
	
	color = gl_Color;
	
	lmcoord = gl_TextureMatrix[1] * gl_MultiTexCoord1;
	
	gl_FogFragCoord = gl_Position.z;
	
	tangent = vec3(0.0);
	binormal = vec3(0.0);
	normal = normalize(gl_NormalMatrix * gl_Normal);

	if (gl_Normal.x > 0.5) {
		//  1.0,  0.0,  0.0
		tangent  = normalize(gl_NormalMatrix * vec3( 0.0,  0.0, -1.0));
		binormal = normalize(gl_NormalMatrix * vec3( 0.0, -1.0,  0.0));
	} else if (gl_Normal.x < -0.5) {
		// -1.0,  0.0,  0.0
		tangent  = normalize(gl_NormalMatrix * vec3( 0.0,  0.0,  1.0));
		binormal = normalize(gl_NormalMatrix * vec3( 0.0, -1.0,  0.0));
	} else if (gl_Normal.y > 0.5) {
		//  0.0,  1.0,  0.0
		tangent  = normalize(gl_NormalMatrix * vec3( 1.0,  0.0,  0.0));
		binormal = normalize(gl_NormalMatrix * vec3( 0.0,  0.0,  1.0));
	} else if (gl_Normal.y < -0.5) {
		//  0.0, -1.0,  0.0
		tangent  = normalize(gl_NormalMatrix * vec3( 1.0,  0.0,  0.0));
		binormal = normalize(gl_NormalMatrix * vec3( 0.0,  0.0,  1.0));
	} else if (gl_Normal.z > 0.5) {
		//  0.0,  0.0,  1.0
		tangent  = normalize(gl_NormalMatrix * vec3( 1.0,  0.0,  0.0));
		binormal = normalize(gl_NormalMatrix * vec3( 0.0, -1.0,  0.0));
	} else if (gl_Normal.z < -0.5) {
		//  0.0,  0.0, -1.0
		tangent  = normalize(gl_NormalMatrix * vec3(-1.0,  0.0,  0.0));
		binormal = normalize(gl_NormalMatrix * vec3( 0.0, -1.0,  0.0));
	}
	

	
	mat3 tbnMatrix = mat3(tangent.x, binormal.x, normal.x,
                          tangent.y, binormal.y, normal.y,
                          tangent.z, binormal.z, normal.z);
	
	viewVector = (gl_ModelViewMatrix * gl_Vertex).xyz;
	viewVector = normalize(tbnMatrix * viewVector);
	
}